package cn.addenda.se.argreslog;

import cn.addenda.se.argreslog.ArgResLogAttr.ArgResLogAttrBuilder;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
@Slf4j
public class ArgResLogMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass());
        ArgResLog argResLog = AnnotationUtils.findAnnotation(method, ArgResLog.class);
        if (argResLog == null) {
            return invocation.proceed();
        }

        // 从AOP过来的，参数以及返回值无法做到函数式那样详细。
        // 但Spring AOP的粒度是方法，所以类名+方法名已够用。
        String callerInfo = method.getDeclaringClass().getSimpleName() + "#" + method.getName();

        ArgResLogAttr attr = ArgResLogAttrBuilder.newBuilder()
            .withLogParam(argResLog.logParam())
            .withLogResult(argResLog.logResult())
            .withLogCost(argResLog.logCost())
            .withIgnoreNullResult(argResLog.ignoreNullResult())
            .withExceptionLevel(argResLog.exceptionLevel()).build();

        try {
            return ArgResLogSupport.invoke(attr, invocation.getArguments(), invocation::proceed, callerInfo);
        }
        // invoke在三种情况下会发生异常：
        // 第一种情况：ArgRes的使用不正确，此时内部异常为SystemException，这类异常直接扔出去。
        // 第二种情况：executor内部的异常，此时原始异常会被包装成ArgResLogException。
        //           当原始异常为RuntimeException时，将原始异常抛出。
        // 第三种情况：ArgRes功能本身出现的异常，这类异常直接扔出去。理论上不会发生。此时不走catch块。
        catch (ArgResLogException argResLogException) {
            throw ArgResLogSupport.unWarp(argResLogException);
        }

    }

}
