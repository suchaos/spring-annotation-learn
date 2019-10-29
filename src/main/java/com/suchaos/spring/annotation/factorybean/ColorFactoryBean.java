package com.suchaos.spring.annotation.factorybean;

import com.suchaos.spring.annotation.bean.Color;
import org.springframework.beans.factory.FactoryBean;

/**
 * 使用 Spring 提供的 `FactoryBean`（工厂 Bean）给容器中注册组件
 *
 * @author suchao
 * @date 2019/10/29
 */
public class ColorFactoryBean implements FactoryBean<Color> {
    @Override
    public Color getObject() throws Exception {
        return new Color();
    }

    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
