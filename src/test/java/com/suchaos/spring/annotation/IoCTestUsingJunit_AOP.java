package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.aop.MathCalculator;
import com.suchaos.spring.annotation.bean.Boss;
import com.suchaos.spring.annotation.bean.Car;
import com.suchaos.spring.annotation.bean.Dog;
import com.suchaos.spring.annotation.config.MainConfigOfAOP;
import com.suchaos.spring.annotation.dao.BookDao;
import com.suchaos.spring.annotation.service.BookService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * AOP
 *
 * @author suchao
 * @date 2019/10/30
 */
public class IoCTestUsingJunit_AOP {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);
        System.out.println("容器创建完成");
    }

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void testAOP() {
        MathCalculator calculator = applicationContext.getBean(MathCalculator.class);
        System.out.println(calculator);
        calculator.div(1, 2);
        printLine();
        printBeans();
        printLine();
        printBeansOfType(MathCalculator.class);
        printLine();
        MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);
        System.out.println(mathCalculator);
        //calculator.div(1, 0);
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
