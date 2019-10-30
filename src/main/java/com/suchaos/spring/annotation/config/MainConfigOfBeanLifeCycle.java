package com.suchaos.spring.annotation.config;

import com.suchaos.spring.annotation.bean.Car;
import com.suchaos.spring.annotation.bean.Cat;
import com.suchaos.spring.annotation.bean.Dog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * bean 的生命周期：bean 创建 --- 初始化 --- 销毁的过程
 * <p>
 * 容器管理 bean 的生命周期
 * <p>
 * 我们可以自定义初始化和销毁方法，
 * 容器在 bean 进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法
 *
 * @author suchao
 * @date 2019/10/29
 */
@Configuration
@ComponentScan("com.suchaos.spring.annotation.beanpostprocessor")
public class MainConfigOfBeanLifeCycle {

    @Bean(initMethod = "init", destroyMethod = "destory")
    public Car car() {
        return new Car();
    }

    @Bean
    public Cat cat() {
        return new Cat();
    }

    @Bean
    public Dog dog() {
        return new Dog();
    }
}
