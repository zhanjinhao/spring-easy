package cn.addenda.se.argreslog;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author addenda
 * @datetime 2023/3/9 14:45
 */
@Slf4j
public class ArgResLogUtilsTest extends AbstractArgResLogTest {

    private ArgResLogTestService argResLogTestService = new ArgResLogTestService();

    @Test
    public void test01() {
        Object o1 = eatThrowable(() -> ArgResLogUtils.doLog(() -> argResLogTestService.completeNormally("aa"), "aa"));

        log.info("\n-----------------------------------------------------------------------------------------\n");
        Object o2 = eatThrowable(() -> ArgResLogUtils.doLog(() -> argResLogTestService.completeBusinessExceptionally("aa"), "aa"));

        log.info("\n------------------------------------------------------------------------------------------\n");
        Object o3 = eatThrowable(() -> ArgResLogUtils.doLog(() -> argResLogTestService.completeCheckedExceptionally("aa"), "aa"));
    }

}
