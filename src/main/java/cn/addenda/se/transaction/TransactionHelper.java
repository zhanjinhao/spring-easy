package cn.addenda.se.transaction;

import cn.addenda.se.result.ServiceException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.Method;

public class TransactionHelper extends TransactionAspectSupport {

    public TransactionHelper() {
        setTransactionAttributeSource(new TransactionHelperAttributeSource());
    }

    public <R> R doTransaction(TransactionAttribute txAttr, TransactionExecutor<R> executor) {
        return _process(txAttr, executor);
    }

    private <R> R _process(TransactionAttribute txAttr, TransactionExecutor<R> executor) {
        TransactionHelperAttributeSource.setTransactionAttribute(txAttr);
        try {
            return (R) invokeWithinTransaction(extractMethod(executor), executor.getClass(), new InvocationCallback() {
                @Override
                public Object proceedWithInvocation() throws Throwable {
                    return executor.process();
                }
            });
        } catch (ServiceException e) {
            throw e;
        } catch (Throwable e) {
            throw new TransactionException("事务在TransactionHelper内执行失败！", e);
        } finally {
            TransactionHelperAttributeSource.clear();
        }
    }

    private Method extractMethod(TransactionExecutor<?> executor) {
        Method[] methods = executor.getClass().getMethods();
        for (Method method : methods) {
            if ("process".equals(method.getName()) && method.getParameterCount() == 0) {
                return method;
            }
        }
        throw new TransactionException("找不到 TransactionExecutor#process() 方法。");
    }

    public interface TransactionExecutor<R> {
        R process() throws Throwable;
    }

    public interface VoidTransactionExecutor {
        void process() throws Throwable;
    }

}
