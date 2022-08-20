package cn.addenda.se.propertyrefresh;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 这个配置类被标注为 ROLE_INFRASTRUCTURE 来压制 BeanPostProcessorChecker#postProcessAfterInitialization 的info。
 * 在此 @Configuration 类中配置的 PropertyRefreshBeanPostProcessor 是晚于 @Configuration 类实例化的。
 * 所以 PropertyRefreshConfiguration 创建时没法 PropertyRefreshBeanPostProcessor 处理。就会出现一个info。
 *
 * @Author ISJINHAO
 * @Date 2022/4/2 20:32
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class PropertyRefreshConfiguration implements ImportAware {

    protected AnnotationAttributes annotationAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
            importMetadata.getAnnotationAttributes(EnablePropertyRefresh.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                "@EnablePropertyRefresh is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    public PropertyRefreshBeanPostProcessor propertyRefreshPostProcessor() {
        PropertyRefreshBeanPostProcessor postProcessor = new PropertyRefreshBeanPostProcessor();
        postProcessor.setOrder(annotationAttributes.getNumber("order"));
        postProcessor.setThreadSizes(annotationAttributes.getNumber("threadSizes"));
        return postProcessor;
    }

}
