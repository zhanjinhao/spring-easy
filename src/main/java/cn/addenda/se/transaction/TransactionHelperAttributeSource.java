package cn.addenda.se.transaction;

import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.lang.reflect.Method;

/**
 * @author ISJINHAO
 * @date 2022/2/27
 */
public class TransactionHelperAttributeSource implements TransactionAttributeSource {

    private static final ThreadLocal<TransactionAttribute> transactionAttributeThreadLocal = new ThreadLocal<>();

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        return transactionAttributeThreadLocal.get();
    }

    public static void setTransactionAttribute(TransactionAttribute transactionAttribute) {
        transactionAttributeThreadLocal.set(transactionAttribute);
    }

    public static void clear() {
        transactionAttributeThreadLocal.remove();
    }

}
