package com.suchaos.spring.annotation.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 声明式事务相关
 *
 * 环境搭建：
 *     1. 导入相关依赖：数据源，数据库驱动，Spring-jdbc 模块
 *     2. 配置数据源，JdbcTemplate
 *     3. 方法或类上标注 @Transactional
 *     4. 开启事务管理功能：@EnableTransactionManagement
 *     5. 配置事务管理器来控制事务
 *
 * @author suchao
 * @date 2019/11/1
 */
@Configuration
@ComponentScan("com.suchaos.spring.annotation.tx")
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class MainConfigOfTx {

    @Bean
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
        // 下面这种方法也可以，而且 Spring 对 @Configuration 类会特殊处理，
        // 即使下面调用了方法，也失去容器中找组件，而不是又创建一个新的对象
        // return new JdbcTemplate(dataSource());
    }


    /**
     * 注册事务管理器到容器中
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
