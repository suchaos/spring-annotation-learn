package com.suchaos.spring.annotation.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 可以使用  JSR250 规范定义的注解
 * <p>
 * 1.  {@link PostConstruct}：在 bean 创建完成并且属性赋值完成，来执行初始化
 * <p>
 * 2.  {@link PreDestroy}：在容器销毁 bean 之前，通知我们进行清理工作
 *
 * @author suchao
 * @date 2019/10/29
 */
public class Dog {

    public Dog() {
        System.out.println("Dog constructor");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Dog PreDestroy");
    }

    @PostConstruct
    public void init() throws Exception {
        System.out.println("Dog PostConstruct");
    }
}
