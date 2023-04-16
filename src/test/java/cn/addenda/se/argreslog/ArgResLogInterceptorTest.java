package cn.addenda.se.argreslog;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author addenda
 * @datetime 2023/3/9 14:45
 */
@Slf4j
public class ArgResLogInterceptorTest extends AbstractArgResLogTest {

    AnnotationConfigApplicationContext context;

    ArgResLogTestService argResLogTestService;

    @Before
    public void before() {
        context = new AnnotationConfigApplicationContext();
        context.register(ArgResLogTestConfiguration.class);
        context.register(ArgResLogTestService.class);
        context.refresh();

        argResLogTestService = context.getBean(ArgResLogTestService.class);
    }

    @After
    public void after() {
        context.close();
    }

    @Test
    public void testOnlyAop() {
        Object o1 = eatThrowable(() -> argResLogTestService.completeNormally("AA"));
        log.info("\n-----------------------------------------------------------------------------------------\n");
        Object o2 = eatThrowable(() -> argResLogTestService.completeBusinessExceptionally("AA"));
        log.info("\n------------------------------------------------------------------------------------------\n");
        Object o3 = eatThrowable(() -> {
            try {
                return argResLogTestService.completeCheckedExceptionally("AA");
            } catch (SQLException e) {
                return null;
            }
        });

    }

    @Test
    public void testAopInUtils() {
        Object o1 = eatThrowable(() -> {
            return ArgResLogUtils.doLog(() -> argResLogTestService.completeNormally("AA"), "AA");
        });
        log.info("\n-----------------------------------------------------------------------------------------\n");
        Object o2 = eatThrowable(() -> {
            return ArgResLogUtils.doLog(() -> argResLogTestService.completeBusinessExceptionally("AA"), "AA");
        });
        log.info("\n------------------------------------------------------------------------------------------\n");
        Object o3 = eatThrowable(() -> {
            return ArgResLogUtils.doLog(() -> argResLogTestService.completeCheckedExceptionally("AA"), "AA");
        });
        log.info("\n------------------------------------------------------------------------------------------\n");
        Object o4 = eatThrowable(() -> argResLogTestService.completeNormally("AA"));
    }

    @Test
    public void testUtilsInAop() {
        Object o1 = eatThrowable(() -> argResLogTestService.completeNormally2("AA"));
        log.info("\n-----------------------------------------------------------------------------------------\n");

        Object o2 = eatThrowable(() -> argResLogTestService.completeBusinessExceptionally2("AA"));
        log.info("\n------------------------------------------------------------------------------------------\n");

        Object o3 = eatThrowable(() -> {
            try {
                return argResLogTestService.completeCheckedExceptionally2("AA");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }


}
