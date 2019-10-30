package com.suchaos.spring.annotation.config;

import com.suchaos.spring.annotation.bean.Boss;
import com.suchaos.spring.annotation.bean.Car;
import com.suchaos.spring.annotation.bean.Dog;
import com.suchaos.spring.annotation.dao.BookDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Autowired 自动装配
 * <p>
 * Spring 利用依赖注入（DI），完成对 IoC 容器中各个组件的依赖关系赋值
 *
 * @author suchao
 * @date 2019/10/30
 */
@Configuration
@ComponentScan({"com.suchaos.spring.annotation.controller",
        "com.suchaos.spring.annotation.service", "com.suchaos.spring.annotation.dao"})
public class MainConfigOfAutowired {

    @Bean("bookDao2")
    @Primary
    public BookDao bookDao() {
        BookDao bookDao = new BookDao("@Bean");
        return bookDao;
    }

    @Bean
    public Car car() {
        return new Car();
    }

    @Bean
    public Dog dog() {
        return new Dog();
    }

    @Bean
    public Boss boss() {
        return new Boss();
    }

}
