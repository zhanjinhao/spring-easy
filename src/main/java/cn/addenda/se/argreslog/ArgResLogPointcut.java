package cn.addenda.se.argreslog;

import java.lang.reflect.Method;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class ArgResLogPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        ArgResLog annotation = AnnotationUtils.findAnnotation(targetClass, ArgResLog.class);
        if (annotation == null) {
            Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            annotation = AnnotationUtils.findAnnotation(actualMethod, ArgResLog.class);
        }

        return annotation != null;
    }
}
