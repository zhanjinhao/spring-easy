package cn.addenda.se.result;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @Author ISJINHAO
 * @Date 2022/2/27 22:25
 */
public class ServiceResultPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        ServiceResultConvertible annotation = AnnotationUtils.findAnnotation(actualMethod, ServiceResultConvertible.class);
        if (annotation != null) {
            Class<?> returnType = actualMethod.getReturnType();
            if (ServiceResult.class.isAssignableFrom(returnType)) {
                if (ServiceResultConvertible.ERROR_TO_FAILED.equals(annotation.errorTo())) {
                    return true;
                } else if (ServiceResultConvertible.ERROR_TO_SUCCESS.equals(annotation.errorTo())) {
                    return true;
                } else {
                    throw new ResultException("only support ServiceResultConvertible.ERROR_TO_FAILED and ServiceResultConvertible.ERROR_TO_SUCCESS.");
                }
            } else {
                throw new ResultException("标注 ServiceResultConvertible 注解的方法的返回值需要为 cn.addenda.se.result.ServiceResult.");
            }
        }

        return false;
    }

}
