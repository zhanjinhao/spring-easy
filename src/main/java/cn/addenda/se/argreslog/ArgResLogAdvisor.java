package cn.addenda.se.argreslog;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * @author addenda
 * @datetime 2022/9/29 13:52
 */
public class ArgResLogAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    @Override
    public Pointcut getPointcut() {
        return new ArgResLogPointcut();
    }
}
