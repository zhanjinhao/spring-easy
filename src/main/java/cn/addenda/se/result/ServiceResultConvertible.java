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

    String ERROR_TO_SUCCESS = "errorToSuccess";
    String ERROR_TO_FAILED = "errorToFailed";

    // 哪些异常能进行转换
    Class<? extends Throwable> exceptionClass() default RuntimeException.class;

    String errorMsg() default "error occurred!";

    String errorTo() default ERROR_TO_FAILED;

}
