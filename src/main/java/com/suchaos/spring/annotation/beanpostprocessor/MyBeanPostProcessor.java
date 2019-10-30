package com.suchaos.spring.annotation.beanpostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link BeanPostProcessor} ：bean 的后置处理器，在 bean 初始化前后进行一些清理工作
 * <p>
 * * postProcessBeforeInitialization：在初始化之前工作
 * <p>
 * * postProcessAfterInitialization：在初始化之后工作
 *
 * @author suchao
 * @date 2019/10/29
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization... " + beanName+" ==> " + bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization... " + beanName+" ==> " + bean);
        return bean;
    }
}
