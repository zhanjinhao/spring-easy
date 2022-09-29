package cn.addenda.se.paramreslog;

import java.lang.reflect.Method;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class ParamResLogPointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        ParamResLog annotation = AnnotationUtils.findAnnotation(targetClass, ParamResLog.class);
        if (annotation == null) {
            Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
            annotation = AnnotationUtils.findAnnotation(actualMethod, ParamResLog.class);
        }

        return annotation != null;
    }
}
