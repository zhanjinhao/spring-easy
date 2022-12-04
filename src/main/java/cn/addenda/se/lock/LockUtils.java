package cn.addenda.se.lock;

import cn.addenda.businesseasy.lock.LockService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.Assert;

/**
 * @author addenda
 * @datetime 2022/12/1 19:18
 */
public class LockUtils implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private String namespace;
    private LockService lockService;

    public LockUtils() {
    }

    public LockUtils(String namespace) {
        this.namespace = namespace;
    }

    public LockUtils(String namespace, LockService lockService) {
        this.namespace = namespace;
        this.lockService = lockService;
    }

    private static ApplicationContext applicationContext;
    private static LockHelper lockHelper;

    private static final String SPRING_NOT_START_MSG = "LockUtils不能在Spring启动过程中使用。";

    /**
     * 最简单的加锁场景，arguments[0] 是 key
     */
    public static <R> R doLock(LockHelper.LockExecutor<R> executor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        return doLock(0, executor, arguments);
    }

    public static void doLock(LockHelper.VoidLockExecutor voidExecutor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        doLock(0, voidExecutor, arguments);
    }

    /**
     * 较上一个场景，arguments[keyArgumentIndex] 是 key
     */
    public static <R> R doLock(int keyArgumentIndex, LockHelper.LockExecutor<R> executor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        return doLock(keyArgumentIndex, LockAttribute.DEFAULT_PREFIX, executor, arguments);
    }

    public static void doLock(int keyArgumentIndex, LockHelper.VoidLockExecutor voidExecutor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        doLock(keyArgumentIndex, LockAttribute.DEFAULT_PREFIX, voidExecutor, arguments);
    }

    /**
     * 较上一个场景，arguments[keyArgumentIndex] 是 key，prefix可以指定
     */
    public static <R> R doLock(int keyArgumentIndex, String prefix, LockHelper.LockExecutor<R> executor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        LockAttribute attribute = LockAttribute.lockAttributeBuilder
                .newBuilder().withPrefix(prefix).withKeyArgumentIndex(keyArgumentIndex).build();
        return doLock(attribute, executor, arguments);
    }

    public static void doLock(int keyArgumentIndex, String prefix, LockHelper.VoidLockExecutor voidExecutor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        LockAttribute attribute = LockAttribute.lockAttributeBuilder
                .newBuilder().withPrefix(prefix).withKeyArgumentIndex(keyArgumentIndex).build();
        doLock(attribute, voidExecutor, arguments);
    }

    /**
     * 较上一个场景，所有参数都可以指定。
     */
    public static <R> R doLock(LockAttribute attribute, LockHelper.LockExecutor<R> executor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        return lockHelper.doLock(attribute, executor, arguments);
    }

    public static void doLock(LockAttribute attribute, LockHelper.VoidLockExecutor voidExecutor, Object... arguments) {
        Assert.notNull(lockHelper, SPRING_NOT_START_MSG);
        LockHelper.LockExecutor<?> executor = () -> {
            voidExecutor.process();
            return null;
        };
        doLock(attribute, executor, arguments);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        lockHelper = applicationContext.getBean(LockHelper.class);
        lockHelper.setNamespace(namespace);
        if (lockService != null) {
            lockHelper.setDefaultLockService(lockService);
        } else {
            lockHelper.setDefaultLockService(applicationContext.getBean(LockService.class));
        }
    }

}
