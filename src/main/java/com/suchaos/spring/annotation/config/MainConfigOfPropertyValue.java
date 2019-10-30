package com.suchaos.spring.annotation.config;

import com.suchaos.spring.annotation.bean.Car;
import com.suchaos.spring.annotation.bean.Cat;
import com.suchaos.spring.annotation.bean.Dog;
import com.suchaos.spring.annotation.bean.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 属性赋值相关的内容：@Value, @PropertySource
 *
 * @author suchao
 * @date 2019/10/30
 */
@Configuration
@PropertySource(name = "使用 PropertySource 注入配置文件", value = {"classpath:student.properties"}, encoding = "UTF-8")
public class MainConfigOfPropertyValue {

    @Bean
    public Student student() {
        return new Student();
    }
}
