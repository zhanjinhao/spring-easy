package cn.addenda.se.result;

import cn.addenda.se.pojo.TxTest;
import cn.addenda.se.service.TxTestService;
import cn.addenda.se.service.TxTestServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author ISJINHAO
 * @Date 2022/2/26 23:00
 */
public class ServiceResultConvertibleTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ResultDeclareConfiguration.class, TxTestServiceImpl.class);

        context.refresh();

        TxTestService txTestService = context.getBean(TxTestService.class);

        System.out.println(txTestService.insert(new TxTest("springDeclare11111111111111111111111", "aha44")));

        context.close();
    }

}
