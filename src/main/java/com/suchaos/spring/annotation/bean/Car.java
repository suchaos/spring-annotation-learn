package com.suchaos.spring.annotation.bean;

/**
 * 用来演示自定义初始化和销毁的方法
 *
 * @author suchao
 * @date 2019/10/29
 */
public class Car {

    public Car() {
        System.out.println("car constructor...");
    }

    public void init() {
        System.out.println("car ... init ...");
    }

    public void destory() {
        System.out.println("car ... destory ...");
    }
}
