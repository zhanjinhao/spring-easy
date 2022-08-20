package cn.addenda.se.multidatasource;

import cn.addenda.se.pojo.TxTest;
import cn.addenda.se.service.TxTestService;
import cn.addenda.se.service.TxTestServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author ISJINHAO
 * @Date 2022/2/26 23:00
 */
public class MultiDataSourceConvertibleTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(MultiDataSourceDeclareConfiguration.class, TxTestServiceImpl.class);

        context.refresh();

        TxTestService txTestService = context.getBean(TxTestService.class);

        System.out.println(txTestService.insert(new TxTest("MultiDataSource", "aha44")));

        context.close();
    }

}
