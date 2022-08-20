package cn.addenda.se.multidatasource;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Author ISJINHAO
 * @Date 2022/3/3 18:58
 */
public interface SlaveDataSourceSelector {

    DataSource select(String key, List<DataSource> dataSourceList);

}
