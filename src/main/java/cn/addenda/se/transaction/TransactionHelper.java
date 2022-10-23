package cn.addenda.se.transaction;

import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.Method;

public class TransactionHelper extends TransactionAspectSupport {

    public TransactionHelper() {
        setTransactionAttributeSource(new TransactionHelperAttributeSource());
    }

    /**
     * 最简单的事务控制场景（当发生任何异常（Exception.class）都回滚事务），
     */
    public <R> R doTransaction(TransactionExecutor<R> executor) {
        return doTransaction(Exception.class, executor);
    }


    /**
     * 较上一个场景，该场景可以指定针对特定的异常类型发生事务回滚
     */
    public <R> R doTransaction(Class<? extends Throwable> rollbackFor, TransactionExecutor<R> executor) {
        TransactionAttribute transactionAttribute = TransactionAttributeBuilder.newBuilder().rollbackFor(rollbackFor).build();
        return doTransaction(transactionAttribute, executor);
    }

    /**
     * 最复杂的场景，需要手动指定所有的事务控制参数。
     */
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
