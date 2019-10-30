package com.suchaos.spring.annotation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP 使用
 *
 * @author suchao
 * @date 2019/10/30
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.suchaos.spring.annotation.aop")
public class MainConfigOfAOP {
}
