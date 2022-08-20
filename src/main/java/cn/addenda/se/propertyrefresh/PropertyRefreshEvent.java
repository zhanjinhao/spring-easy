package cn.addenda.se.propertyrefresh;

import org.springframework.context.ApplicationEvent;

/**
 * @Author ISJINHAO
 * @Date 2022/4/3 10:30
 */
public class PropertyRefreshEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public PropertyRefreshEvent(Object source) {
        super(source);
    }
}
