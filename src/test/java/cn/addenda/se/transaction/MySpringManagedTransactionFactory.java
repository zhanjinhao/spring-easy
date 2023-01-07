
package cn.addenda.se.transaction;

import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.mybatis.spring.transaction.SpringManagedTransaction;


public class MySpringManagedTransactionFactory implements TransactionFactory {

  @Override
  public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
    return new MySpringManagedTransaction(dataSource);
  }


  @Override
  public Transaction newTransaction(Connection conn) {
    throw new UnsupportedOperationException("New Spring transactions require a DataSource");
  }


  @Override
  public void setProperties(Properties props) {
    // not needed in this version
  }

}
