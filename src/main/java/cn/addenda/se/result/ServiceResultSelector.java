package cn.addenda.se.result;

import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author ISJINHAO
 * @Date 2022/3/1 12:05
 */
public class ServiceResultSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                AutoProxyRegistrar.class.getName(),
                ServiceResultConfiguration.class.getName()};
    }

}
