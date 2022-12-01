package cn.addenda.se.transaction;

import cn.addenda.se.mapper.TxTestMapper;
import cn.addenda.se.pojo.TxTest;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ISJINHAO
 * @date 2022/2/27
 */
public class TransactionUtilsTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:cn/addenda/se/transaction/spring-transactionhelper-context.xml");

        SqlSessionFactory sqlSessionFactory = context.getBean(SqlSessionFactory.class);
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.SIMPLE);
        TxTestMapper txTestMapper = sqlSession.getMapper(TxTestMapper.class);
        Integer integer = TransactionUtils.doTransaction(Exception.class, () -> {
            return txTestMapper.insert(new TxTest("VoidTxExecutor", "123"));
        });
        System.out.println(integer);

        TransactionUtils.doTransaction(Exception.class, () -> {
            txTestMapper.insert(new TxTest("VoidTxExecutor", "123"));
        });

    }

}
