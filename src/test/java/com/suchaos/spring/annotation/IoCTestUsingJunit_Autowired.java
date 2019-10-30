package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.bean.Boss;
import com.suchaos.spring.annotation.bean.Car;
import com.suchaos.spring.annotation.bean.Dog;
import com.suchaos.spring.annotation.config.MainConfigOfAutowired;
import com.suchaos.spring.annotation.dao.BookDao;
import com.suchaos.spring.annotation.service.BookService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * Autowired 自动装配相关的内容
 *
 * @author suchao
 * @date 2019/10/30
 */
public class IoCTestUsingJunit_Autowired {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() {
        //applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);
        System.out.println("容器创建完成");
    }

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void testAutowired() {
        BookService bookService = applicationContext.getBean(BookService.class);
        bookService.printDao();

        String[] names = applicationContext.getBeanNamesForType(BookDao.class);
        System.out.println(Arrays.toString(names));

        printLine();
        printBeans();


//        BookDao bookDao = applicationContext.getBean(BookDao.class);
//        System.out.println(bookDao);
    }

    @Test
    public void test02() {
        Boss boss = applicationContext.getBean(Boss.class);
        System.out.println(boss);

        Car car = applicationContext.getBean(Car.class);
        Assert.assertSame(boss.getCar(), car);

        Dog dog = applicationContext.getBean(Dog.class);
        Assert.assertSame(boss.getDog(), dog);
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
