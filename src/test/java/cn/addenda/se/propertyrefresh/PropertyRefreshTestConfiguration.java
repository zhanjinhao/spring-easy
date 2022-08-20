package cn.addenda.se.propertyrefresh;

import org.springframework.context.annotation.PropertySource;

/**
 * @Author ISJINHAO
 * @Date 2022/2/27 14:03
 */
@PropertySource(value = {"classpath:propertyrefreshtest.properties"})
@EnablePropertyRefresh
public class PropertyRefreshTestConfiguration {

}
