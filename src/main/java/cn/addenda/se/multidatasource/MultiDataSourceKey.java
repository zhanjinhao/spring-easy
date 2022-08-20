package cn.addenda.se.multidatasource;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiDataSourceKey {

    String dataSourceName() default MultiDataSourceConstant.DEFAULT;

    String mode() default MultiDataSourceConstant.MASTER;

}
