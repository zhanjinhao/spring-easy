package cn.addenda.se.propertyrefresh;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author ISJINHAO
 * @Date 2022/4/2 20:31
 */
public class PropertyRefreshSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                        PropertyRefreshConfiguration.class.getName()
                };
    }

}
