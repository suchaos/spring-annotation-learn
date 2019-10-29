package com.suchaos.spring.annotation.config;

import com.suchaos.spring.annotation.bean.Color;
import com.suchaos.spring.annotation.bean.Person;
import com.suchaos.spring.annotation.condition.LinuxCondition;
import com.suchaos.spring.annotation.condition.WindowsCondition;
import com.suchaos.spring.annotation.factorybean.ColorFactoryBean;
import com.suchaos.spring.annotation.selector.MyImportBeanDefinitionRegistrar;
import com.suchaos.spring.annotation.selector.MyImportSelector;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

/**
  这个类用来学习：@Scope, @Lazy, @Conditional, @Import

  @author suchao
 * @date 2019/10/29
 */
@Configuration
@Conditional({WindowsCondition.class})
@Import({Color.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class MainConfig2 {

    /*
     * 注意：默认是单实例的
     *
     *  @see ConfigurableBeanFactory#SCOPE_PROTOTYPE
     *  @see ConfigurableBeanFactory#SCOPE_SINGLETON
     *
     *  下面这两个需要 web 环境
     *  @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST
     *  @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION
     *
     *  prototype: 多实例的
     *  singleton: 单实例的（默认值）： IoC容器启动会调用方法创建对象放到容器中
     *                                 以后每次获取就是直接从容器(map.get())中拿
     *  request: 同一次请求创建一个实例：Ioc容器启动并不会调用方法创建对象放在容器中
     *                                  而是每次获取的时候才会调用方法创建对象
     *  session: 同一个 session 创建一个实例
     *
     *  懒加载：
     *  单实例 bean：默认在容器启动的时候创建对象，懒加载则是使得 容器在启动时不创建，而是
     *  第一次使用（获取）Bean时创建对象，并初始化
     *
     */

    //@Scope(scopeName = "prototype")
    @Bean(name = "person")
    @Lazy
    public Person person() {
        System.out.println("给容器中添加 person");
        return Person.builder().age(15).name("张三").build();
    }

    /*
     * {@link Conditional} 按照一定条件进行判断，满则条件给容器中注册 Bean
     * 1. 放到方法上：如果满足条件，则执行方法，也就是注册 bean
     * 2. 放到类上，如果满足条件，这类中配种的所有 bean 注册才能生效。类中组件统一设置
     */

    /**
     * 判断条件：如果是 Windows，注册这个 Bean
     * @return bill person bean
     */
    @Bean(name = "bill")
    @Conditional({WindowsCondition.class})
    public Person person01() {
        return Person.builder().name("Bill").age(65).build();
    }

    /**
     * 判断条件：如果是 linux，注册这个 Bean
     * @return Linux person bean
     */
    @Bean(name = "linux")
    @Conditional({LinuxCondition.class})
    public Person person02() {
        return Person.builder().name("Linux").age(48).build();
    }

    @Bean
    public ColorFactoryBean colorFactoryBean() {
        return new ColorFactoryBean();
    }
}
