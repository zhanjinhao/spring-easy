package cn.addenda.se.propertyrefresh;

import java.lang.annotation.*;

/**
 * 注解必须和@Value同时存在
 *
 * @Author ISJINHAO
 * @Date 2022/2/28 17:55
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyRefresh {

    /**
     * 单位 s，-1表示取 @EnablePropertyFresh 的delay值
     *
     * @return
     */
    int delay() default 10;

    boolean nullAble() default false;

}
