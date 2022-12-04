package cn.addenda.se.lock;

import java.lang.annotation.*;

/**
 * @author addenda
 * @datetime 2022/9/29 14:00
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Locked {

    int keyArgumentIndex() default 0;

    String prefix() default LockAttribute.DEFAULT_PREFIX;

    String keyExtractor() default LockAttribute.DEFAULT_KEY_EXTRACTOR;

    String lockFailedMsg() default LockAttribute.DEFAULT_LOCK_FAILED_MSG;

}
