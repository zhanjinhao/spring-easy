package cn.addenda.se.result;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @Author ISJINHAO
 * @Date 2022/2/27 22:20
 */
public class ServiceResultMethodInterceptor implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceResultMethodInterceptor.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = AopUtils.getMostSpecificMethod(invocation.getMethod(), invocation.getThis().getClass());
        ServiceResultConvertible serviceResultConvertible = AnnotationUtils.findAnnotation(method, ServiceResultConvertible.class);
        if (serviceResultConvertible == null) {
            return invocation.proceed();
        }

        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable throwable) {
            // 这个是可以被转换的异常
            if (serviceResultConvertible.exceptionClass().isAssignableFrom(throwable.getClass())) {
                if (serviceResultConvertible.errorToFailed()) {
                    ServiceResult<?> objectServiceResult = new ServiceResult<>(ServiceResultStatus.FAILED, null);
                    objectServiceResult.setErrorMsg(serviceResultConvertible.errorMsg());
                    objectServiceResult.setEndTm(System.currentTimeMillis());
                    logger.error("", throwable);
                    return objectServiceResult;
                } else if (serviceResultConvertible.errorToSuccess()) {
                    ServiceResult<?> objectServiceResult = new ServiceResult<>(ServiceResultStatus.SUCCESS, null);
                    objectServiceResult.setEndTm(System.currentTimeMillis());
                    logger.error("", throwable);
                    return objectServiceResult;
                } else {
                    throw new ResultException("ServiceResultConvertible 的 errorToSuccess ^ errorToFailed 需要为true。");
                }
            } else {
                throw throwable;
            }
        }
        return result;
    }

}
