package cn.addenda.se.lock;

import cn.addenda.businesseasy.lock.LockService;
import cn.addenda.businesseasy.util.ExceptionUtil;
import cn.addenda.se.result.ServiceException;
import java.lang.reflect.UndeclaredThrowableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author addenda
 * @datetime 2022/12/1 18:47
 */
@Slf4j
public class LockAspectSupport implements BeanFactoryAware {

    private String namespace = "default";

    private LockService defaultLockService;

    private BeanFactory beanFactory;

    private final ExpressionParser parser = new SpelExpressionParser();

    public Object invokeWithinLock(LockAttribute lockAttribute, Object[] arguments, TSupplier<Object> supplier) throws Throwable {

        int parameterIndex = lockAttribute.getKeyArgumentIndex();
        if (parameterIndex >= arguments.length) {
            throw new LockException("当前方法参数个数为" + arguments.length + "，指定的分布锁使用的参数index为" + parameterIndex + "。");
        }

        LockService lockService;
        if (defaultLockService != null) {
            lockService = defaultLockService;
        } else {
            lockService = beanFactory.getBean(LockService.class);
        }

        Object keyArgument = arguments[parameterIndex];
        String key;
        if (keyArgument.getClass().isPrimitive() || keyArgument.getClass().isAssignableFrom(CharSequence.class)) {
            key = keyArgument.toString();
        } else {
            StandardEvaluationContext context = new StandardEvaluationContext(keyArgument);
            key = parser.parseExpression(lockAttribute.getKeyExtractor()).getValue(context, String.class);
        }
        if (key == null || key.length() == 0) {
            throw new LockException("不能对 null 或 \"\" 加分布式锁。");
        }

        String lockedKey = namespace + ":" + lockAttribute.getPrefix() + ":" + key;
        if (!lockService.tryLock(lockedKey)) {
            throw new ServiceException(lockAttribute.getLockFailedMsg().replace("{}", lockedKey));
        } else {
            try {
                log.debug("分布式锁 [{}] 加锁成功。", lockedKey);
                return supplier.get();
            } catch (Throwable e) {
                log.error("分布式锁 [" + lockedKey + "] 加锁期间，业务执行失败！");
                throw e;
            } finally {
                boolean releaseSuccess = true;
                try {
                    lockService.unlock(lockedKey);
                } catch (Throwable throwable) {
                    log.error("分布式锁 [{}] 释放失败。", lockedKey, throwable);
                    releaseSuccess = false;
                }
                if (releaseSuccess) {
                    log.debug("分布式锁 [{}] 释放成功。", lockedKey);
                }
            }
        }
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setDefaultLockService(LockService defaultLockService) {
        this.defaultLockService = defaultLockService;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public interface TSupplier<T> {

        T get() throws Throwable;
    }

    protected static void reportAsRuntimeException(Throwable throwable) {
        throwable = ExceptionUtil.unwrapThrowable(throwable);
        if (!(throwable instanceof LockException)) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new UndeclaredThrowableException(throwable);
            }
        }

        throw (LockException) throwable;
    }

}
