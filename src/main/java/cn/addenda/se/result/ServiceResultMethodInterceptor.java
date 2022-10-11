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
        ServiceResult<?> result;
        try {
            result = (ServiceResult<?>) invocation.proceed();
        } catch (Throwable throwable) {
            // 不可被转换的异常
            if (!serviceResultConvertible.excClass().isAssignableFrom(throwable.getClass())) {
                // 不可被转换的异常，DEBUG模式输出异常。
                if (logger.isDebugEnabled()) {
                    logger.debug("", throwable);
                }
                throw throwable;
            }

            // 可被转换的异常
            ServiceException serviceException = (ServiceException) throwable;
            if (logger.isDebugEnabled()) {
                // 可被转换的异常，DEBUG模式输出异常。
                logger.debug(serviceException.getExcMsg(), throwable);
            }

            if (ServiceResultConvertible.EXC_TO_ERROR.equals(serviceResultConvertible.excTo())) {
                result = ServiceResult.error(null, serviceException.getExcMsg());
                if (logger.isErrorEnabled()) {
                    logger.error(serviceException.getExcMsg(), throwable);
                }
            } else if (ServiceResultConvertible.EXC_TO_SUCCESS.equals(serviceResultConvertible.excTo())) {
                result = ServiceResult.success(null);
            } else {
                throw new ServiceResultException("only support EXC_TO_ERROR and EXC_TO_SUCCESS. ");
            }
        }

        // 在未发生异常时，可能返回 null
        if (result != null) {
            result.setStartTs(startTime);
            result.setEndTs(System.currentTimeMillis());
        }
        return result;
    }

}
