package cn.addenda.se.propertyrefresh;

import cn.addenda.businesseasy.concurrent.SimpleNamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.*;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 这个BeanPostProcessor必须放在 AutowiredAnnotationBeanPostProcessor 后面执行
 *
 * @Author ISJINHAO
 * @Date 2022/4/3 11:00
 */
public class PropertyRefreshBeanPostProcessor implements ApplicationListener<ApplicationContextEvent>, MergedBeanDefinitionPostProcessor, ApplicationEventPublisherAware, ApplicationContextAware, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(PropertyRefreshBeanPostProcessor.class);

    private ApplicationEventPublisher applicationEventPublisher;

    private final Map<String, List<PropertyRefreshHolder>> listenedFieldMap = new ConcurrentHashMap<>();

    private int threadSizes = 1;

    private ApplicationContext applicationContext;

    private DefaultListableBeanFactory defaultListableBeanFactory;

    private ScheduledExecutorService scheduledExecutorService;

    private static final Class<? extends Annotation> propertyRefreshClass = PropertyRefresh.class;
    private static final Class<? extends Annotation> valueClass = Value.class;

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        ReflectionUtils.doWithFields(beanType, field -> {

            Annotation[] annotations = field.getAnnotations();

            PropertyRefresh propertyRefresh = null;
            Value value = null;

            for (Annotation annotation : annotations) {
                if (propertyRefreshClass.equals(annotation.annotationType())) {
                    propertyRefresh = (PropertyRefresh) annotation;
                    continue;
                }
                if (valueClass.equals(annotation.annotationType())) {
                    value = (Value) annotation;
                    continue;
                }
                // @Value 和 @PropertyRefresh 都找到时可以退出了
                if (propertyRefresh != null && value != null) {
                    break;
                }
            }

            if (propertyRefresh != null && !beanDefinition.isSingleton()) {
                throw new PropertyRefreshException(
                        "@PropertyRefresh can only used in singleton bean, the scope of current beanType [" + beanType.getName() + "] is " + beanDefinition.getScope());
            }

            if (value == null && propertyRefresh == null) {
                return;
            }

            if (value != null && propertyRefresh == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "The field [{}] of [{}] dose not be refreshed automatically! Using @PropertyRefresh to activate automatic refresh. ", field.getName(), beanType);
                }
                return;
            }

            // propertyRefresh is present now.
            if (value == null) {
                throw new PropertyRefreshException(
                        "@PropertyRefresh must be used together with @Value. beanType: [" + beanType.getName() + "], fieldName: [" + field.getName() + "]. ");
            }

            // -----------------------------------------------------------
            // valuePresent is present and propertyRefreshPresent is present now
            // -----------------------------------------------------------

            if (!PropertyRefreshListener.class.isAssignableFrom(beanType)) {
                throw new PropertyRefreshException(
                        "[" + beanType + "] exists property which is registered as automatic refresh, must implement @PropertyFresh interface. ");
            }

            List<PropertyRefreshHolder> propertyRefreshHolderList =
                    listenedFieldMap.computeIfAbsent(beanName, c -> new ArrayList<>());

            PropertyRefreshHolder holder = new PropertyRefreshHolder();
            holder.setBeanName(beanName);
            holder.setField(field);
            holder.setDelay(propertyRefresh.delay());
            holder.setNullAble(propertyRefresh.nullAble());
            holder.setExpression(value.value());
            holder.setBeanType(beanType);
            propertyRefreshHolderList.add(holder);

            logger.info("The field [{}] of [{}] has registered for automatic refresh! Refresh period: per [{}] seconds. ", field.getName(), beanType, holder.getDelay());
        });
    }

    public void setThreadSizes(int threadSizes) {
        this.threadSizes = threadSizes;
    }

    private int order;

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void onApplicationEvent(ApplicationContextEvent event) {
        if (ContextRefreshedEvent.class.isAssignableFrom(event.getClass())) {
            fillPropertyRefreshHolder();
            initBeanFactory();

            scheduledExecutorService = Executors.newScheduledThreadPool(threadSizes, new SimpleNamedThreadFactory("spring-easy: propertyrefresh"));
            Set<Map.Entry<String, List<PropertyRefreshHolder>>> entries = listenedFieldMap.entrySet();
            for (Map.Entry<String, List<PropertyRefreshHolder>> entry : entries) {
                List<PropertyRefreshHolder> propertyRefreshHolderList = entry.getValue();
                propertyRefreshHolderList.forEach(holder -> scheduledExecutorService.scheduleAtFixedRate(() -> {
                    Object value;
                    try {
                        DependencyDescriptor desc = new DependencyDescriptor(holder.getField(), false);
                        desc.setContainingClass(holder.getBeanType());
                        Set<String> autowiredBeanNames = new LinkedHashSet<>(1);
                        TypeConverter typeConverter = defaultListableBeanFactory.getTypeConverter();
                        value = defaultListableBeanFactory.resolveDependency(desc, holder.getBeanName(), autowiredBeanNames, typeConverter);
                    } catch (Exception e) {
                        throw new PropertyRefreshException("cannot resolve value from applicationContext, [" + holder.getField() + "]", e);
                    }

                    holder.setOldValue(holder.getNewValue());
                    holder.setNewValue(value);

                    Object oldValue = holder.getOldValue();
                    Object newValue = value;
                    if (!Objects.equals(oldValue, newValue)) {
                        logger.info("property changed! beanName: [{}], field: [{}], expression: [{}], beanType: [{}], oldValue: [{}], newValue: [{}]",
                                holder.getBeanName(), holder.getField(), holder.getExpression(), holder.getBeanType(), holder.getOldValue(), holder.getNewValue());
                        applicationEventPublisher.publishEvent(new PropertyRefreshEvent(holder));
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("property do not change! [{}]", holder);
                        }
                    }
                }, holder.getDelay(), holder.getDelay(), TimeUnit.SECONDS));
            }
        } else if (ContextClosedEvent.class.isAssignableFrom(event.getClass())) {
            logger.info("Spring context close event has received, close PropertyRefresh executor service.");
            if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
                scheduledExecutorService.shutdown();
            }
        }
    }

    private void initBeanFactory() {
        this.defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }

    private void fillPropertyRefreshHolder() {
        Set<Map.Entry<String, List<PropertyRefreshHolder>>> entries = listenedFieldMap.entrySet();
        Iterator<Map.Entry<String, List<PropertyRefreshHolder>>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<PropertyRefreshHolder>> entry = iterator.next();
            String beanName = entry.getKey();
            // fieldList 不可能为 null 或 isEmpty()
            List<PropertyRefreshHolder> fieldList = entry.getValue();
            ObjectProvider<?> beanProvider = applicationContext.getBeanProvider(fieldList.get(0).getBeanType());
            Object bean = beanProvider.getIfAvailable();
            if (bean == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("cannot find bean named [{}] in applicationContext", beanName);
                }
                iterator.remove();
            } else {
                for (PropertyRefreshHolder propertyRefreshHolder : fieldList) {
                    propertyRefreshHolder.setBean(bean);

                    Field field = propertyRefreshHolder.getField();
                    field.setAccessible(true);
                    try {
                        propertyRefreshHolder.setNewValue(field.get(bean));
                    } catch (IllegalAccessException e) {
                        throw new PropertyRefreshException("cannot set value to the field [" + propertyRefreshHolder.getField() + "]", e);
                    }
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
