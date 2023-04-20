package cn.addenda.se.transaction;

import cn.addenda.businesseasy.util.ExceptionUtil;
import java.lang.reflect.UndeclaredThrowableException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @date 2022/4/8
 */
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
        } catch (Throwable throwable) {
            Throwable unwrapThrowable = ExceptionUtil.unwrapThrowable(throwable);
            if (!(unwrapThrowable instanceof RuntimeException)) {
                throw new UndeclaredThrowableException(unwrapThrowable);
            }
            throw (RuntimeException) unwrapThrowable;
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
