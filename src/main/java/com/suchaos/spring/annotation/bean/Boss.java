package com.suchaos.spring.annotation.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.util.StringValueResolver;

/**
 * 演示 {@link Autowired}：用于 构造器，参数，方法，属性
 *
 * @author suchao
 * @date 2019/10/30
 */
@ToString
@Getter
public class Boss implements ApplicationContextAware, BeanNameAware, EmbeddedValueResolverAware {

    private Car car;

    private Dog dog;

    @Autowired
    public void setCarAndDog(Car car, Dog dog) {
        this.car = car;
        this.dog = dog;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("BeanNameAware: " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ApplicationContextAware: " + applicationContext);
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        System.out.println("EmbeddedValueResolverAware: " +
                resolver.resolveStringValue("你好，我是${user.name}, 年龄是 #{18-1}"));
    }
}
