package cn.addenda.se.paramreslog;

import java.lang.annotation.*;

/**
 * @author addenda
 * @datetime 2022/9/29 14:00
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamResLoggable {

    boolean logParam() default true;

    boolean logReturn() default true;

    boolean logCost() default true;

}
