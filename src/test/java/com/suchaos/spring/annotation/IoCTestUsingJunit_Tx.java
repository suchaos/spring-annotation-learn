package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.aop.MathCalculator;
import com.suchaos.spring.annotation.config.MainConfigOfTx;
import com.suchaos.spring.annotation.tx.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.InfrastructureAdvisorAutoProxyCreator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 测试事务
 *
 * @author suchao
 * @date 2019/11/1
 */
public class IoCTestUsingJunit_Tx {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(MainConfigOfTx.class);
        System.out.println("容器创建完成");
    }

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void testTx() {
        UserService userService = applicationContext.getBean(UserService.class);
        userService.insertUser();

        printBeans();
        printLine();
        printBeansOfType(InfrastructureAdvisorAutoProxyCreator.class);
        printLine();
        printBeansOfType(AnnotationAwareAspectJAutoProxyCreator.class);
    }

    private void printBeans() {
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    private void printBeansOfType(Class<?> type) {
        String[] names = applicationContext.getBeanNamesForType(type);
        for (String name : names) {
            System.out.println(name);
        }
    }

    private void printLine() {
        System.out.println("=================");
    }
}
