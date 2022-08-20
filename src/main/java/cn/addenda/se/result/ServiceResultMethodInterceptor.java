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

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = invocation.proceed();
        } catch (Throwable throwable) {
            // 不可以被转换的异常
            if (!serviceResultConvertible.exceptionClass().isAssignableFrom(throwable.getClass())) {
                throw throwable;
            }
            ServiceResult<?> objectServiceResult;
            // 可以被转换的异常
            if (ServiceResultConvertible.ERROR_TO_FAILED.equals(serviceResultConvertible.errorTo())) {
                objectServiceResult = new ServiceResult<>(ServiceResultStatus.FAILED, null);
                objectServiceResult.setErrorMsg(serviceResultConvertible.errorMsg());
            } else if (ServiceResultConvertible.ERROR_TO_SUCCESS.equals(serviceResultConvertible.errorTo())) {
                objectServiceResult = new ServiceResult<>(ServiceResultStatus.SUCCESS, null);
            } else {
                throw new ResultException("only support ServiceResultConvertible.ERROR_TO_FAILED and ServiceResultConvertible.ERROR_TO_SUCCESS.");
            }
            objectServiceResult.setStartTm(startTime);
            objectServiceResult.setEndTm(System.currentTimeMillis());
            logger.error("", throwable);
            return objectServiceResult;
        }
        return result;
    }

}
