package cn.addenda.se.propertyrefresh;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @Author ISJINHAO
 * @Date 2022/2/28 17:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PropertyRefreshSelector.class)
public @interface EnablePropertyRefresh {

    int order() default Ordered.LOWEST_PRECEDENCE;

    int threadSizes() default 1;

}
