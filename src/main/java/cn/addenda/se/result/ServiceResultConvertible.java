package cn.addenda.se.result;

import java.lang.annotation.*;

/**
 * 这个注解作用于Service层的方法。会与 @Transactional 等事务或缓存注解并存。
 * 当方法被多次提升时，这个注解应该在最后生效，即在所有的通知中，此注解对应的通知最后执行。
 */
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServiceResultConvertible {

    // 哪些error能强制转换为failed
    Class<? extends Throwable> exceptionClass() default RuntimeException.class;

    boolean errorToFailed() default true;

    String errorMsg() default "error occurred!";

    boolean errorToSuccess() default false;

}
