package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.aop.MathCalculator;
import com.suchaos.spring.annotation.config.MainConfigOfAOP;
import com.suchaos.spring.annotation.config.MainConfigOfAOPForChild;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * AOP 父子容器演示
 *
 * @author suchao
 * @date 2019/10/30
 */
public class IoCTestUsingJunit_AOPForChild {

    AnnotationConfigApplicationContext applicationContextParent;
    AnnotationConfigApplicationContext applicationContextChild;

    @Before
    public void setUp() {
        applicationContextParent = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);
        applicationContextChild = new AnnotationConfigApplicationContext(MainConfigOfAOPForChild.class);
        applicationContextChild.setParent(applicationContextParent);
        System.out.println("容器创建完成");
    }

    @After
    public void tearDown() {
        applicationContextParent.close();
        applicationContextChild.close();
    }

    @Test
    public void testAOP() {
        MathCalculator calculator = applicationContextParent.getBean(MathCalculator.class);
        System.out.println("parent bean:" + calculator);
        calculator.div(1, 2);
        printLine();
        calculator = applicationContextChild.getBean(MathCalculator.class);
        System.out.println("child bean:" + calculator);
        calculator.div(1, 2);
        //calculator.div(1, 0);
    }

    private void printBeans() {
        String[] names = applicationContextParent.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    private void printBeansOfType(Class<?> type) {
        String[] names = applicationContextParent.getBeanNamesForType(type);
        for (String name : names) {
            System.out.println(name);
        }
    }

    private void printLine() {
        System.out.println("=================");
    }
}
