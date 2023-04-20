package cn.addenda.se.transaction;

import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import java.lang.reflect.Method;
import java.util.Stack;

/**
 * @author addenda
 * @date 2022/2/27
 */
public class TransactionHelperAttributeSource implements TransactionAttributeSource {

    private static final ThreadLocal<Stack<TransactionAttribute>> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        return THREAD_LOCAL.get().peek();
    }

    public static void setTransactionAttribute(TransactionAttribute transactionAttribute) {
        Stack<TransactionAttribute> transactionAttributes = THREAD_LOCAL.get();
        if (transactionAttributes == null) {
            transactionAttributes = new Stack<>();
            THREAD_LOCAL.set(transactionAttributes);
        }
        transactionAttributes.push(transactionAttribute);
    }

    public static void clear() {
        Stack<TransactionAttribute> transactionAttributes = THREAD_LOCAL.get();
        transactionAttributes.pop();
        if (transactionAttributes.isEmpty()) {
            THREAD_LOCAL.remove();
        }
    }

}
