package cn.addenda.se.argreslog;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class ArgResLogConfiguration implements ImportAware {

    protected AnnotationAttributes annotationAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
            importMetadata.getAnnotationAttributes(EnableArgResLog.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                "@EnableArgResLog is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ArgResLogAdvisor argResLogAdvisor() {
        ArgResLogAdvisor argResLogAdvisor = new ArgResLogAdvisor();
        argResLogAdvisor.setAdvice(new ArgResLogMethodInterceptor());
        if (this.annotationAttributes != null) {
            argResLogAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
        }
        return argResLogAdvisor;
    }

}
