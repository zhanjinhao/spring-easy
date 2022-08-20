package cn.addenda.se.multidatasource;

import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @Author ISJINHAO
 * @Date 2022/3/2 23:04
 */
public class MultiDataSourcePointcut extends StaticMethodMatcherPointcut {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        Method actualMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        return AnnotationUtils.findAnnotation(actualMethod, MultiDataSourceKey.class) != null;
    }

}
