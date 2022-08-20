package cn.addenda.se.springcontext;

import cn.addenda.se.pojo.TxTest;
import cn.addenda.se.service.TxTestService;
import cn.addenda.se.service.TxTestServiceImpl;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author ISJINHAO
 * @Date 2022/3/2 18:18
 */
public class ApplicationContextUtilTest {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(SpringContextDeclareConfiguration.class, ApplicationContextUtils.class);

        context.refresh();

        TxTestService txTestService = new TxTestServiceImpl();

        ApplicationContextUtils applicationContextUtil = context.getBean(ApplicationContextUtils.class);
        txTestService = (TxTestService) applicationContextUtil.autowiredInstanceByType(txTestService);
        applicationContextUtil.registerSingletonBean(txTestService);

        TxTestService bean = applicationContextUtil.getBean(TxTestService.class);

        System.out.println(bean.insert(new TxTest("springDeclare11111111111111111111111", "aha44")));

        context.close();

    }

}
