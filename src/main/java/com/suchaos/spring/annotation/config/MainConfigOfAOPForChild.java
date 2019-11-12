package com.suchaos.spring.annotation.config;

import com.suchaos.spring.annotation.aop.LogAspect;
import com.suchaos.spring.annotation.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP 使用，父子容器演示
 *
 * @author suchao
 * @date 2019/10/30
 */
@Configuration
@EnableAspectJAutoProxy
//@ComponentScan({"com.suchaos.spring.annotation.aop", "com.suchaos.spring.annotation.beanpostprocessor"})
public class MainConfigOfAOPForChild {

    @Bean
    public LogAspect logAspect() {
        return new LogAspect();
    }

    @Bean
    public MathCalculator mathCalculator() {
        return new MathCalculator();
    }
}
