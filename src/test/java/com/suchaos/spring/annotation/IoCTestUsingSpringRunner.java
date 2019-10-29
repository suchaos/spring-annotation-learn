package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.config.MainConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * IoCTest
 *
 * @author suchao
 * @date 2019/10/29
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MainConfig.class)
public class IoCTestUsingSpringRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test01() {
        // AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        String[] names = applicationContext.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}
