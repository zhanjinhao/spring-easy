package cn.addenda.se.transaction;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * @author addenda
 * @datetime 2023/1/7 14:12
 */
public class TransactionHelperAttributeSourceTest {

    public static void main(String[] args) {
        TransactionAttribute transactionAttribute1 = TransactionAttributeBuilder.newRRBuilder().build();
        TransactionHelperAttributeSource.setTransactionAttribute(transactionAttribute1);
        TransactionAttribute transactionAttribute2 = TransactionAttributeBuilder
                .newBuilder()
                .rollbackFor(Exception.class)
                .withIsolation(Isolation.READ_COMMITTED)
                .build();
        TransactionHelperAttributeSource.setTransactionAttribute(transactionAttribute2);

        TransactionHelperAttributeSource transactionHelperAttributeSource = new TransactionHelperAttributeSource();
        System.out.println(transactionHelperAttributeSource.getTransactionAttribute(null, null));
        TransactionHelperAttributeSource.clear();
        System.out.println(transactionHelperAttributeSource.getTransactionAttribute(null, null));
        TransactionHelperAttributeSource.clear();
    }
}
