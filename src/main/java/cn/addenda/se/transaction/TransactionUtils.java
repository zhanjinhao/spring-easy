package cn.addenda.se.transaction;

import cn.addenda.se.transaction.TransactionHelper.TransactionExecutor;
import cn.addenda.se.transaction.TransactionHelper.VoidTransactionExecutor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.util.Assert;

public class TransactionUtils implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private String transactionManagerBeanName;
    private TransactionManager transactionManager;

    private static ApplicationContext applicationContext;
    private static TransactionHelper transactionHelper;

    private static final String SPRING_NOT_START_MSG = "TransactionUtils不能在Spring启动过程中使用。";

    public TransactionUtils() {
    }

    public TransactionUtils(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public TransactionUtils(String transactionManagerBeanName) {
        this.transactionManagerBeanName = transactionManagerBeanName;
    }

    public TransactionUtils(String transactionManagerBeanName, TransactionManager transactionManager) {
        this.transactionManagerBeanName = transactionManagerBeanName;
        this.transactionManager = transactionManager;
    }

    /**
     * 最简单的事务控制场景（当发生任何异常（Exception.class）都回滚事务），
     */
    public static <R> R doTransaction(TransactionExecutor<R> executor) {
        Assert.notNull(transactionHelper, SPRING_NOT_START_MSG);
        return doTransaction(Exception.class, executor);
    }

    public static void doTransaction(VoidTransactionExecutor voidExecutor) {
        Assert.notNull(transactionHelper, SPRING_NOT_START_MSG);
        doTransaction(Exception.class, voidExecutor);
    }

    /**
     * 较上一个场景，该场景可以指定针对特定的异常类型发生事务回滚
     */
    public static <R> R doTransaction(Class<? extends Throwable> rollbackFor, TransactionExecutor<R> executor) {
        Assert.notNull(transactionHelper, SPRING_NOT_START_MSG);
        TransactionAttribute attribute = TransactionAttributeBuilder.newBuilder().rollbackFor(rollbackFor).build();
        return doTransaction(attribute, executor);
    }

    public static void doTransaction(Class<? extends Throwable> rollbackFor, VoidTransactionExecutor voidExecutor) {
        Assert.notNull(transactionHelper, SPRING_NOT_START_MSG);
        TransactionAttribute attribute = TransactionAttributeBuilder.newBuilder().rollbackFor(rollbackFor).build();
        doTransaction(attribute, voidExecutor);
    }

    /**
     * 最复杂的场景，需要手动指定所有的事务控制参数，TransactionAttribute 可以通过 TransactionAttributeBuilder构造
     * TransactionAttributeBuilder的入参跟@Transactional注解的参数保持一致
     */
    public static <R> R doTransaction(TransactionAttribute txAttr, TransactionExecutor<R> executor) {
        Assert.notNull(transactionHelper, SPRING_NOT_START_MSG);
        return transactionHelper.doTransaction(txAttr, executor);
    }

    public static void doTransaction(TransactionAttribute txAttr, VoidTransactionExecutor executor) {
        Assert.notNull(transactionHelper, SPRING_NOT_START_MSG);
        TransactionExecutor<?> transactionExecutor = () -> {
            executor.process();
            return null;
        };
        doTransaction(txAttr, transactionExecutor);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        transactionHelper = applicationContext.getBean(TransactionHelper.class);
        if (transactionManager != null) {
            transactionHelper.setTransactionManager(transactionManager);
        }
        if (transactionManagerBeanName != null) {
            transactionHelper.setTransactionManagerBeanName(transactionManagerBeanName);
        }
    }
}
