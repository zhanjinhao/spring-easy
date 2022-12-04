package cn.addenda.se.lock;

import cn.addenda.businesseasy.lock.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * @author addenda
 * @datetime 2022/9/29 13:51
 */
@Configuration
public class LockConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes annotationAttributes;

    private LockService lockService;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.annotationAttributes = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLockManagement.class.getName(), false));
        if (this.annotationAttributes == null) {
            throw new IllegalArgumentException(
                    "@EnableLockManagement is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LockAdvisor lockAdvisor() {
        LockAdvisor lockAdvisor = new LockAdvisor();
        LockMethodInterceptor methodInterceptor = new LockMethodInterceptor();
        lockAdvisor.setAdvice(methodInterceptor);
        if (this.annotationAttributes != null) {
            lockAdvisor.setOrder(annotationAttributes.<Integer>getNumber("order"));
            methodInterceptor.setNamespace(annotationAttributes.<String>getString("namespace"));
        }
        methodInterceptor.setDefaultLockService(lockService);
        return lockAdvisor;
    }

    @Autowired(required = false)
    void setConfigurers(LockConfigurer configurer) {
        lockService = configurer.getLockService();
    }

}
