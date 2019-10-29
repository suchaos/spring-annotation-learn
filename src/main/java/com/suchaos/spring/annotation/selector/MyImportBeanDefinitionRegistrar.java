package com.suchaos.spring.annotation.selector;

import com.suchaos.spring.annotation.bean.RainBow;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * {@link Import} {@link ImportBeanDefinitionRegistrar}
 *
 * @author suchao
 * @date 2019/10/29
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * @param importingClassMetadata：当前类的注解信息
     * @param registry：BeanDefinition         注册类，进行手工注册
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean hasBlue = registry.containsBeanDefinition("com.suchaos.spring.annotation.bean.Blue");
        boolean hasYellow = registry.containsBeanDefinition("com.suchaos.spring.annotation.bean.Yellow");
        if (hasBlue && hasYellow) {
            BeanDefinition definition = new RootBeanDefinition(RainBow.class);
            // 指定 bean 名称
            registry.registerBeanDefinition("rainBow", definition);
        }
    }
}
