package com.suchaos.spring.annotation.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

/**
 * 演示属性赋值 @Value
 *
 * @author suchao
 * @date 2019/10/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    /*
        @Value 赋值：
           1. 基本数值
           2. SpEL, #{}
           3. ${}, 取出 Environment 中的属性值
     */


    @Value("苏超")
    private String name;

    @Value("#{18-2}")
    private Integer age;

    @Value("${student.nickName}")
    private String nickName;
}
