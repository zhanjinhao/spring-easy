package cn.addenda.se.propertyrefresh;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author ISJINHAO
 * @Date 2022/4/2 20:21
 */
public class ScheduledExecutorServiceTest {

    public static void main(String[] args) {

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        for (int i = 0; i < 2; ++i) {
            scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " run ");
                }
            }, 0, 2, TimeUnit.SECONDS);
        }


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
