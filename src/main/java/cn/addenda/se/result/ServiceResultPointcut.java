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
        if (annotation == null) {
            return false;
        }
        Class<?> returnType = actualMethod.getReturnType();
        // 方法的返回值必须是ServiceResult
        if (ServiceResult.class.isAssignableFrom(returnType)) {
            String excTo = annotation.excTo();
            if (!ServiceResultConvertible.EXC_TO_ERROR.equals(excTo)
                    && !ServiceResultConvertible.EXC_TO_SUCCESS.equals(excTo)
                    && !ServiceResultConvertible.EXC_TO_DISPATCH.equals(excTo)) {
                throw new ServiceResultException("only support EXC_TO_ERROR and EXC_TO_SUCCESS and EXC_TO_DISPATCH. ");
            }
            Class<? extends Throwable> exceptionClass = annotation.excClass();
            if (!ServiceException.class.isAssignableFrom(exceptionClass)) {
                throw new ServiceResultException("ServiceResultConvertible.excClass 需要为 cn.addenda.se.result.ServiceException 的子类。");
            }
            return true;
        } else {
            throw new ServiceResultException("标注 ServiceResultConvertible 注解的方法的返回值需要为 cn.addenda.se.result.ServiceResult. ");
        }
    }

}
