package cn.addenda.se.multidatasource;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @Author ISJINHAO
 * @Date 2022/3/2 23:02
 */
public class MultiDataSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    @Override
    public Pointcut getPointcut() {
        return new MultiDataSourcePointcut();
    }
}
