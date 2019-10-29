package com.suchaos.spring.annotation.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 判断是否是 Linux 系统
 *
 * @author suchao
 * @date 2019/10/29
 */
public class LinuxCondition implements Condition {
    /**
     *
     * @param context 判断条件能使用的上下文（环境）
     * @param metadata 注解信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 1. 能获取到 ioc 使用的 beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 2. 获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        // 3. 获取当前环境信息
        Environment environment = context.getEnvironment();
        // 4. 获取到 bean 定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        // 可以判断容器中的 bean 注册情况，也可以给容器中注册 bean
        boolean containsBeanDefinition = registry.containsBeanDefinition("person");

        return environment.getProperty("os.name").toLowerCase().contains("linux");
    }
}
