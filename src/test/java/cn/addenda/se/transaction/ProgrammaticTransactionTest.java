package cn.addenda.se.transaction;

import cn.addenda.se.mapper.TxTestMapper;
import cn.addenda.se.pojo.TxTest;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author addenda
 * @datetime 2023/1/5 20:22
 */
public class ProgrammaticTransactionTest {


    public static void main(String[] args) {
        System.out.println(Isolation.READ_COMMITTED.value());

        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:cn/addenda/se/transaction/spring-transactionhelper-context.xml");

        SqlSessionFactory sqlSessionFactory = context.getBean(SqlSessionFactory.class);
        SqlSession sqlSession = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);
        TxTestMapper txTestMapper = sqlSession.getMapper(TxTestMapper.class);

        PlatformTransactionManager transactionManager = context.getBean(PlatformTransactionManager.class);

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            txTestMapper.insert(new TxTest("ReturnTxExecutor", "123"));
            status.setRollbackOnly();
        } catch (Exception ex) {
            // log ...
            status.setRollbackOnly();
        } finally {
            transactionManager.commit(status);
        }

        context.close();

    }


}
