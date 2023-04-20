package cn.addenda.se.argreslog;

import java.sql.SQLException;

/**
 * @author addenda
 * @datetime 2023/4/17 21:42
 */
public interface IArgResLogTestService {

    @ArgResLog
    String completeNormally(String param);

    String completeBusinessExceptionally(String param);

    @ArgResLog
    String completeCheckedExceptionally(String param) throws SQLException;

    @ArgResLog
    String completeNormally2(String param);

    @ArgResLog
    String completeBusinessExceptionally2(String param);

    @ArgResLog
    String completeCheckedExceptionally2(String param) throws SQLException;
}
