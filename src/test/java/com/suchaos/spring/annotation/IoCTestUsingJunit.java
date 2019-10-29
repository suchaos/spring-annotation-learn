package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.bean.Blue;
import com.suchaos.spring.annotation.bean.Person;
import com.suchaos.spring.annotation.config.MainConfig;
import com.suchaos.spring.annotation.config.MainConfig2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * IoCTest
 *
 * @author suchao
 * @date 2019/10/29
 */
public class IoCTestUsingJunit {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() {
        //applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
    }

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void test01() {
        printBeansOfType(Person.class);
    }

    @Test
    public void test02() {
        System.out.println("IoC 容器创建完成");
        Person person = applicationContext.getBean("person", Person.class);
        Person person2 = applicationContext.getBean("person", Person.class);

        Assert.assertSame("person 不是单实例的", person, person2);
    }

    @Test
    public void test03() {
        printBeans();

        Map<String, Person> persons = applicationContext.getBeansOfType(Person.class);
        System.out.println(persons);
    }

    @Test
    public void test04() {
        printBeans();

        Blue bean = applicationContext.getBean(Blue.class);
        System.out.println(bean);
    }

    @Test
    public void test05() {
        printBeans();

        Object colorFactoryBean = applicationContext.getBean("colorFactoryBean");
        Object colorFactoryBean2 = applicationContext.getBean("colorFactoryBean");
        System.out.println(colorFactoryBean);
        Assert.assertSame(colorFactoryBean, colorFactoryBean2);

        Object bean = applicationContext.getBean("&colorFactoryBean");
        System.out.println(bean);
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
}
