package cn.addenda.se.result;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @Author ISJINHAO
 * @Date 2022/2/27 22:21
 */
public class ServiceResultAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    @Override
    public Pointcut getPointcut() {
        return new ServiceResultPointcut();
    }
}
