package cn.addenda.se.propertyrefresh;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @Author ISJINHAO
 * @Date 2022/4/5 15:06
 */
public class PropertyRefreshApplication {

    // integration test cases in : https://github.com/zhanjinhao/service-consumption/tree/main/nacos-restful-provider

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(PropertyRefreshTestConfiguration.class, PropertyRefreshServiceImpl.class);

        context.refresh();

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        context.close();
    }
}
