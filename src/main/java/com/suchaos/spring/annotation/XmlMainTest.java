package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.bean.Person;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * MainTest
 *
 * @author suchao
 * @date 2019/10/29
 */
public class XmlMainTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("classpath:beans.xml");

        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);
    }
}
