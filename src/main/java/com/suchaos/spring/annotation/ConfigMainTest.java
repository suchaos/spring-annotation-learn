package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.bean.Person;
import com.suchaos.spring.annotation.config.MainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

/**
 * 使用 java config 的方式
 *
 * @author suchao
 * @date 2019/10/29
 */
public class ConfigMainTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);

        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        System.out.println(Arrays.toString(beanNamesForType));
    }
}
