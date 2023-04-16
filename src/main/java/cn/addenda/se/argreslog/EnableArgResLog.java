package cn.addenda.se.argreslog;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * @author addenda
 * @datetime 2022/9/29 13:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ArgResLogSelector.class)
public @interface EnableArgResLog {

    int order() default Ordered.LOWEST_PRECEDENCE;

    boolean proxyTargetClass() default false;

    AdviceMode mode() default AdviceMode.PROXY;
}
