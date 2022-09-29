package cn.addenda.se.paramreslog;

import cn.addenda.se.multidatasource.EnableMultiDataSource;
import cn.addenda.se.multidatasource.MultiDataSource;
import cn.addenda.se.multidatasource.MultiDataSourceConstant;
import cn.addenda.se.multidatasource.MultiDataSourceEntry;
import cn.addenda.se.result.EnableServiceResultConverter;
import com.alibaba.druid.pool.DruidDataSource;
import java.sql.Driver;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringValueResolver;

/**
 * @Author ISJINHAO
 * @Date 2022/2/27 14:03
 */
//@Configuration(proxyBeanMethods = false)
@EnableTransactionManagement(order = Ordered.LOWEST_PRECEDENCE)
@EnableServiceResultConverter(order = Ordered.LOWEST_PRECEDENCE - 1)
@EnableMultiDataSource(order = Ordered.LOWEST_PRECEDENCE - 2)
@EnableParamResLog(order = Ordered.LOWEST_PRECEDENCE - 4)
//@EnableCaching(order = Ordered.LOWEST_PRECEDENCE - 3)
@PropertySource(value = {"classpath:db.properties"})
public class ParamResLogConfiguration implements EmbeddedValueResolverAware {

    private StringValueResolver stringValueResolver;

    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        try {
            druidDataSource.setDriver((Driver) Class.forName(stringValueResolver.resolveStringValue("${db.driver}")).newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        druidDataSource.setUrl(stringValueResolver.resolveStringValue("${db.url}"));
        druidDataSource.setUsername(stringValueResolver.resolveStringValue("${db.username}"));
        druidDataSource.setPassword(stringValueResolver.resolveStringValue("${db.password}"));
        druidDataSource.setMaxActive(1);
        druidDataSource.setInitialSize(1);

        MultiDataSourceEntry multiDataSourceEntry = new MultiDataSourceEntry();
        multiDataSourceEntry.setMaster(druidDataSource);

        MultiDataSource multiDataSource = new MultiDataSource();
        multiDataSource.addMultiDataSourceEntry(MultiDataSourceConstant.DEFAULT, multiDataSourceEntry);
        return multiDataSource;
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setBasePackage("cn.addenda.se.mapper");
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        return mapperScannerConfigurer;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new ClassPathResource("cn/addenda/se/mapper/TxTestMapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
    }

}
