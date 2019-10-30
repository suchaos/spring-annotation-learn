package com.suchaos.spring.annotation.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 通过让 bean 实现  {@link InitializingBean}  这个接口，来定义初始化逻辑；
 * 销毁则是实现  {@link DisposableBean}  接口，来定义销毁逻辑
 *
 * @author suchao
 * @date 2019/10/29
 */
public class Cat implements InitializingBean, DisposableBean{

    public Cat() {
        System.out.println("cat constructor");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("cat destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("cat afterPropertiesSet");
    }
}
