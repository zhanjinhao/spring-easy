package cn.addenda.se.argreslog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author addenda
 * @datetime 2022/9/29 14:00
 */
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgResLog {

    /**
     * 是否打印参数
     */
    boolean logParam() default true;

    /**
     * 是否打印结果
     */
    boolean logResult() default true;

    /**
     * 是否打印耗时
     */
    boolean logCost() default true;

    /**
     * 如果结果为空，是否不打印结果
     */
    boolean ignoreNullResult() default false;

    /**
     * 如果结果异常，异常日志的级别
     */
    String exceptionLevel() default ArgResLogAttr.ERROR;

}
