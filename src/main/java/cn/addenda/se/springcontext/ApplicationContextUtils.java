package cn.addenda.se.springcontext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author ISJINHAO
 * @Date 2022/3/2 15:25
 */
public class ApplicationContextUtils implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    public Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    public Object getBean(String name, Object... args) throws BeansException {
        return applicationContext.getBean(name, args);
    }

    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return applicationContext.getBean(requiredType, args);
    }

    public <T> T autowiredInstanceByType(T object) {
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.autowireBeanProperties(object, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        return (T) autowireCapableBeanFactory.initializeBean(object, object.getClass().getName());
    }

    public <T> T autowiredInstanceByName(T object) {
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.autowireBeanProperties(object, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
        return (T) autowireCapableBeanFactory.initializeBean(object, object.getClass().getName());
    }

    public void autowiredInstanceByConstructor(Object object) {
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        autowireCapableBeanFactory.autowireBeanProperties(object, AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR, false);
        autowireCapableBeanFactory.initializeBean(object, object.getClass().getName());
    }

    public void registerSingletonBean(Object object) {
        registerSingletonBean(object, object.getClass().getName());
    }

    public void registerSingletonBean(Object object, String beanName) {
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        if (beanFactory instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
            defaultListableBeanFactory.registerSingleton(object.getClass().getName(), object);
        } else {
            throw new SpringContextException("只有DefaultListableBeanFactory支持动态注册bean，当前是：" + beanFactory.getClass());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
