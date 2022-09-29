package cn.addenda.se.paramreslog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * @author addenda
 * @datetime 2022/9/29 13:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ParamResLogSelector.class)
public @interface EnableParamResLog {

    int order() default Ordered.LOWEST_PRECEDENCE;
}
