package cn.addenda.se.lock;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class LockPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Locked annotation = AnnotationUtils.findAnnotation(targetClass, Locked.class);
        if (annotation == null) {
            Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            annotation = AnnotationUtils.findAnnotation(actualMethod, Locked.class);
        }

        return annotation != null;
    }
}
