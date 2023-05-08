package cn.addenda.se.lock;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * EnableLockManagement和EnableTransactionManagement一起使用时，
 * 需要保证事务在锁内执行
 *
 * @author addenda
 * @datetime 2022/9/29 13:55
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LockSelector.class)
public @interface EnableLockManagement {

    String namespace() default "default";

    int order() default Ordered.LOWEST_PRECEDENCE;
}
