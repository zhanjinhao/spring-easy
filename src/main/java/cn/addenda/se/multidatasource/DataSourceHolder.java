package cn.addenda.se.multidatasource;

import javax.sql.DataSource;

/**
 * @Author ISJINHAO
 * @Date 2022/3/4 19:39
 */
public class DataSourceHolder {

    private DataSourceHolder() {
    }

    private static final ThreadLocal<String> activeDataSourceKey = new ThreadLocal<>();

    private static final ThreadLocal<DataSource> activeDataSource = new ThreadLocal<>();

    public static void setActiveDataSourceKey(String dataSourceName, String mode) {
        String curActiveDataSourceKey = activeDataSourceKey.get();
        if (curActiveDataSourceKey != null) {
            String key = dataSourceName + "." + mode;
            // key不一样的话报错！
            if (!curActiveDataSourceKey.equals(key)) {
                throw new MultiDataSourceException("一个事务中只能使用一个数据源，curActiveDataSourceKey：" + curActiveDataSourceKey + "，key：" + key + "。");
            }
            // key一样的话什么都不做
            else {
                // nop
            }
        } else {
            activeDataSourceKey.set(dataSourceName + "." + mode);
        }
    }

    public static void setActiveDataSource(DataSource dataSource) {
        activeDataSource.set(dataSource);
    }

    public static DataSource getActiveDataSource() {
        return activeDataSource.get();
    }

    public static String getActiveDataSourceKey() {
        return activeDataSourceKey.get();
    }

    public static void clear() {
        activeDataSourceKey.remove();
        activeDataSource.remove();
    }
}
