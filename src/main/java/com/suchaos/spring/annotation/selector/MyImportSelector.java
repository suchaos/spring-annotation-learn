package com.suchaos.spring.annotation.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 自定义逻辑，返回需要导入的组件
 *
 * @author suchao
 * @date 2019/10/29
 */
public class MyImportSelector implements ImportSelector {
    /**
     *
     * @param importingClassMetadata：当前标注 @Import 注解的类的所有注解信息
     * @return 返回值就是导入到容器中的组件全类名
     */
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{"com.suchaos.spring.annotation.bean.Blue", "com.suchaos.spring.annotation.bean.Yellow"};
    }
}
