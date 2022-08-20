package cn.addenda.se.propertyrefresh;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author ISJINHAO
 * @Date 2022/4/4 11:28
 */
public class PropertyRefreshServiceImpl implements PropertyRefreshService, PropertyRefreshListener, InitializingBean {

    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

    @Value("#{'${addressList}'.split(',')}")
    @PropertyRefresh
    private List<String> addressList;

    @Override
    public void doPropertyRefresh(PropertyRefreshHolder holder) {
        System.out.println(holder);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + Arrays.deepToString(addressList.toArray()));
            }
        }, 0, 2, TimeUnit.SECONDS);
    }
}
