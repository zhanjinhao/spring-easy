package cn.addenda.se.lock;

import cn.addenda.businesseasy.lock.LockService;
import cn.addenda.se.result.ServiceException;
import cn.addenda.se.result.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class LockAspectSupport implements BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(LockAspectSupport.class);

    private String namespace = "default";

    private LockService defaultLockService;

    private BeanFactory beanFactory;

    ExpressionParser parser = new SpelExpressionParser();

    public Object invokeWithinLock(LockAttribute lockAttribute, Object[] arguments, TSupplier<Object> supplier) {

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
            throw new SystemException("不能对 null 或 \"\" 加分布式锁。");
        }

        String lockedKey = namespace + ":" + lockAttribute.getPrefix() + ":" + key;
        if (!lockService.tryLock(lockedKey)) {
            throw new ServiceException(lockAttribute.getLockFailedMsg().replace("{}", lockedKey));
        } else {
            try {
                logger.info("分布式锁 [{}] 加锁成功。", lockedKey);
                return supplier.get();
            } catch (ServiceException e) {
                throw e;
            } catch (Throwable e) {
                throw new LockException("分布式锁 [" + lockedKey + "] 加锁期间，业务执行失败！", e);
            } finally {
                boolean releaseSuccess = true;
                try {
                    lockService.unlock(lockedKey);
                } catch (Throwable throwable) {
                    logger.info("分布式锁 [{}] 释放失败。", lockedKey, throwable);
                    releaseSuccess = false;
                }
                if (releaseSuccess) {
                    logger.info("分布式锁 [{}] 释放成功。", lockedKey);
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

}
