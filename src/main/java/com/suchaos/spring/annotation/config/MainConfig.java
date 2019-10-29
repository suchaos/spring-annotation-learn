package com.suchaos.spring.annotation.config;

import com.suchaos.spring.annotation.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;

/*
  配置类 == 配置文件

  这个类用来学习：@Bean， @Configuration, @ComponentScan（包括其中的自定义的 Filter）

  @author suchao
 * @date 2019/10/29
 */

/**
 * {@link Configuration} 告诉 Spring 这是一个配置类
 */
@Configuration
@ComponentScan(basePackages = "com.suchaos.spring.annotation",
        // useDefaultFilters 默认为 true，扫描给定包下的  @Component @Repository, @Service, or @Controller
        useDefaultFilters = false,
//        excludeFilters = {@ComponentScan.Filter(
//                type = FilterType.ANNOTATION,
//                classes = {Controller.class, Service.class})}
        includeFilters = {@ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = {Controller.class, Repository.class})}
)
public class MainConfig {

    /**
     * 给容器中注册一个 Bean；类型为返回值的类型，id 默认是用方法名作为 id
     *
     * @return Person Bean
     */
    @Bean(name = "person")

    public Person person01() {
        return Person.builder().age(20).name("mike").build();
    }
}
