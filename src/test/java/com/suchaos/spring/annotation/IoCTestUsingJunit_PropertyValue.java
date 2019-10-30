package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.bean.Student;
import com.suchaos.spring.annotation.config.MainConfigOfBeanLifeCycle;
import com.suchaos.spring.annotation.config.MainConfigOfPropertyValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 测试 属性赋值
 *
 * @author suchao
 * @date 2019/10/30
 */
public class IoCTestUsingJunit_PropertyValue {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() {
        //applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        applicationContext = new AnnotationConfigApplicationContext(MainConfigOfPropertyValue.class);
        System.out.println("容器创建完成");
    }

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void test() {
        printBeans();
        printLine();
        Student student = applicationContext.getBean("student", Student.class);
        System.out.println(student);
        printLine();
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        environment.getPropertySources().forEach(propertySource -> {
            System.out.println(propertySource.getName() + " ::: " +  propertySource.getSource());
            printLine();
        });
    }

    private void printBeans() {
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    private void printLine() {
        System.out.println("=================");
    }

}
