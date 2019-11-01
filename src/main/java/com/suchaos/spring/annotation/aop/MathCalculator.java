package com.suchaos.spring.annotation.aop;

import org.springframework.stereotype.Component;

/**
 * 用来演示 AOP，这个是业务逻辑类
 *
 * @author suchao
 * @date 2019/10/30
 */
public class MathCalculator {

    public int div(int i, int j) {
        return i / j;
    }

    public int sub(int i, int j) {
        return i - j;
    }
}
