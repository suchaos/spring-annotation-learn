package com.suchaos.spring.annotation;

import com.suchaos.spring.annotation.config.MainConfigOfBeanLifeCycle;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 测试 bean 生命周期
 *
 * @author suchao
 * @date 2019/10/29
 */
public class IoCTestUsingJunit_LifeCycle {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void setUp() {
        //applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        applicationContext = new AnnotationConfigApplicationContext(MainConfigOfBeanLifeCycle.class);
        System.out.println("容器创建完成");
    }

    @After
    public void tearDown() {
        applicationContext.close();
    }

    @Test
    public void testInitAndDestory() {

    }

}
