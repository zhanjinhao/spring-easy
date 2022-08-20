package cn.addenda.se.propertyrefresh;

import org.springframework.context.ApplicationListener;

/**
 * 用户类应该实现这个接口并在 {@link PropertyRefreshListener#doPropertyRefresh(cn.addenda.se.propertyrefresh.PropertyRefreshHolder)()} 里实现更新逻辑
 *
 * @Author ISJINHAO
 * @Date 2022/4/3 10:30
 */
public interface PropertyRefreshListener extends ApplicationListener<PropertyRefreshEvent> {

    @Override
    default void onApplicationEvent(PropertyRefreshEvent event) {
        PropertyRefreshHolder holder = (PropertyRefreshHolder) event.getSource();
        if (holder.getBeanType().isAssignableFrom(this.getClass())) {
            doPropertyRefresh(holder);
        }
    }

    void doPropertyRefresh(PropertyRefreshHolder holder);

}
