package cn.addenda.se.transaction;

import org.apache.ibatis.transaction.Transaction;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.mybatis.spring.transaction.SpringManagedTransaction;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.springframework.util.Assert.notNull;

/**
 * @author addenda
 * @datetime 2023/1/4 23:19
 */
public class MySpringManagedTransaction implements Transaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringManagedTransaction.class);

    private final DataSource dataSource;

    private Connection connection;

    private boolean isConnectionTransactional;

    private boolean autoCommit;

    public MySpringManagedTransaction(DataSource dataSource) {
        notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() throws SQLException {
        openConnection();
        return this.connection;
    }

    /**
     * Gets a connection from Spring transaction manager and discovers if this {@code Transaction} should manage
     * connection or let it to Spring.
     * <p>
     * It also reads autocommit setting because when using Spring Transaction MyBatis thinks that autocommit is always
     * false and will always call commit/rollback so we need to no-op that calls.
     */
    private void openConnection() throws SQLException {
        this.connection = DataSourceUtils.getConnection(this.dataSource);
        this.autoCommit = this.connection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);

        LOGGER.debug(() -> "JDBC Connection [" + this.connection + "] will"
                + (this.isConnectionTransactional ? " " : " not ") + "be managed by Spring");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() throws SQLException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            LOGGER.debug(() -> "Committing JDBC Connection [" + this.connection + "]");
            this.connection.commit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() throws SQLException {
        if (this.connection != null && !this.isConnectionTransactional && !this.autoCommit) {
            LOGGER.debug(() -> "Rolling back JDBC Connection [" + this.connection + "]");
            this.connection.rollback();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SQLException {
        DataSourceUtils.releaseConnection(this.connection, this.dataSource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getTimeout() throws SQLException {
        ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(dataSource);
        if (holder != null && holder.hasTimeout()) {
            return holder.getTimeToLiveInSeconds();
        }
        return null;
    }

}
