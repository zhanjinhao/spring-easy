package cn.addenda.se.result;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * @Author ISJINHAO
 * @Date 2022/3/1 12:11
 */
@Configuration
public class ServiceResultConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes annotationAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableServiceResultConversion.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                    "@EnableServiceResultConverter is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ServiceResultAdvisor serviceResultAdvisor() {
        ServiceResultAdvisor serviceResultAdvisor = new ServiceResultAdvisor();
        serviceResultAdvisor.setAdvice(new ServiceResultMethodInterceptor());
        if (this.annotationAttributes != null) {
            serviceResultAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
        }
        return serviceResultAdvisor;
    }

}
