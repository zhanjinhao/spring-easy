package cn.addenda.se.lock;

import cn.addenda.businesseasy.util.ExceptionUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class LockMethodInterceptor extends LockAspectSupport implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> aClass = invocation.getThis().getClass();
        Locked locked = AnnotationUtils.findAnnotation(aClass, Locked.class);
        if (locked == null) {
            Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), aClass);
            locked = AnnotationUtils.findAnnotation(method, Locked.class);
        }
        if (locked == null) {
            return invocation.proceed();
        }

        LockAttribute attribute = LockAttribute.LockAttributeBuilder.newBuilder()
            .withKeyArgumentIndex(locked.keyArgumentIndex())
            .withKeyExtractor(locked.keyExtractor())
            .withLockFailedMsg(locked.lockFailedMsg())
            .withPrefix(locked.prefix())
            .build();

        try {
            return invokeWithinLock(attribute, invocation.getArguments(), invocation::proceed);
        } catch (Throwable throwable) {
            throw ExceptionUtil.unwrapThrowable(throwable);
        }
    }

}
