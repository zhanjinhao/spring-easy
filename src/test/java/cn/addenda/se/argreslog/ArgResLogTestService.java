package cn.addenda.se.argreslog;

import cn.addenda.se.result.ServiceException;
import java.sql.SQLException;

/**
 * @author addenda
 * @datetime 2023/3/9 16:19
 */
public class ArgResLogTestService {

    @ArgResLog
    protected String completeNormally(String param) {
        return param + " hengha";
    }

    @ArgResLog
    protected String completeBusinessExceptionally(String param) {
        throw new ServiceException(param + " hengha");
    }

    @ArgResLog
    protected String completeCheckedExceptionally(String param) throws SQLException {
        throw new SQLException(param + " hengha");
    }

    @ArgResLog
    protected String completeNormally2(String param) {
        return ArgResLogUtils.doLog(() -> param + " hengha", param);
    }

    @ArgResLog
    protected String completeBusinessExceptionally2(String param) {
        return ArgResLogUtils.doLog(() -> {
            throw new ServiceException(param + " hengha");
        }, param);
    }

    @ArgResLog
    protected String completeCheckedExceptionally2(String param) throws SQLException {
        return ArgResLogUtils.doLog(() -> {
            throw new SQLException(param + " hengha");
        }, param);
    }

}
