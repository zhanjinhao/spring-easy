package cn.addenda.se.transaction;

import cn.addenda.se.mapper.TxTestMapper;
import cn.addenda.se.pojo.TxTest;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author ISJINHAO
 * @date 2022/2/27
 */
public class TransactionTemplateTest {

    public static void main(String[] args) {
        System.out.println(Isolation.READ_COMMITTED.value());

        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:cn/addenda/se/transaction/spring-transactionhelper-context.xml");

        SqlSessionFactory sqlSessionFactory = context.getBean(SqlSessionFactory.class);
        SqlSession sqlSession = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);
        TxTestMapper txTestMapper = sqlSession.getMapper(TxTestMapper.class);

        TransactionTemplate transactionTemplate = context.getBean(TransactionTemplate.class);
        Integer integer = transactionTemplate.execute(status -> {
            try {
                txTestMapper.insert(new TxTest("VoidTxExecutor", "123"));
            } catch (Exception e) {
                e.printStackTrace();
                status.setRollbackOnly();
            }
            return null;
        });
        System.out.println(integer);

        transactionTemplate.executeWithoutResult(status -> {
            try {
                txTestMapper.insert(new TxTest("VoidTxExecutor", "123"));
            } catch (Exception e) {
                e.printStackTrace();
                status.setRollbackOnly();
            }
        });

        context.close();

    }

}
