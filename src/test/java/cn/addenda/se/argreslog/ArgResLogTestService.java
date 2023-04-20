package cn.addenda.se.argreslog;

import cn.addenda.se.result.ServiceException;
import java.sql.SQLException;

/**
 * @author addenda
 * @datetime 2023/3/9 16:19
 */
public class ArgResLogTestService implements IArgResLogTestService {
    @Override
    @ArgResLog
    public String completeNormally(String param) {
        return param + " hengha";
    }

    @Override
    @ArgResLog
    public String completeBusinessExceptionally(String param) {
        throw new ServiceException(param + " hengha");
    }
    @Override
    @ArgResLog
    public String completeCheckedExceptionally(String param) throws SQLException {
        throw new SQLException(param + " hengha");
    }
    @Override
    @ArgResLog
    public String completeNormally2(String param) {
        return ArgResLogUtils.doLog(() -> param + " hengha", param);
    }
    @Override
    @ArgResLog
    public String completeBusinessExceptionally2(String param) {
        return ArgResLogUtils.doLog(() -> {
            throw new ServiceException(param + " hengha");
        }, param);
    }
    @Override
    @ArgResLog
    public String completeCheckedExceptionally2(String param) throws SQLException {
        return ArgResLogUtils.doLog(() -> {
            throw new SQLException(param + " hengha");
        }, param);
    }

}
