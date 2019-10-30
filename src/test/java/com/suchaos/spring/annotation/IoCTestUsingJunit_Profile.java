package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.config.MainConfigOfProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * {@link Profile} 相关的内容
 *
 *  1. -Dspring.profiles.active=test
 *
 * @author suchao
 * @date 2019/10/30
 */
public class IoCTestUsingJunit_Profile {

    AnnotationConfigApplicationContext applicationContext;

    //@Before
    public void setUp() {
        applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);
        System.out.println("容器创建完成");
    }

    @Before
    public void setUp2() {
        // 1. 创建一个 applicationContext
        applicationContext = new AnnotationConfigApplicationContext();
        // 2. 设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("test", "dev");
        // 3. 注册主配置类
        applicationContext.register(MainConfigOfProfile.class);
        // 4. 启动刷新容器
        applicationContext.refresh();
        System.out.println("容器创建完成");
    }

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void testProfile() {
        String[] names = applicationContext.getBeanNamesForType(DataSource.class);
        for (String name : names) {
            System.out.println(name);
        }
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
