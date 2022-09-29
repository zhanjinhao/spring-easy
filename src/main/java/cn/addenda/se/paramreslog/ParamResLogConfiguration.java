package cn.addenda.se.paramreslog;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
public class ParamResLogConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes annotationAttributes;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
            importMetadata.getAnnotationAttributes(EnableParamResLog.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                "@EnableParamResLog is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ParamResLogAdvisor paramResLogAdvisor() {
        ParamResLogAdvisor paramResLogAdvisor = new ParamResLogAdvisor();
        paramResLogAdvisor.setAdvice(new ParamResLogMethodInterceptor());
        if (this.annotationAttributes != null) {
            paramResLogAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
        }
        return paramResLogAdvisor;
    }

}
