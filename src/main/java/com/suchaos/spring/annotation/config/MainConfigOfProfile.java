package com.suchaos.spring.annotation.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringValueResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * {@link Profile} 学习
 * <p>
 * 这个类中多种读取属性只是都进行演示一些
 *
 * @author suchao
 * @date 2019/10/30
 */
@Configuration
@PropertySource("classpath:/dbconfig.properties")
// @Profile 如果写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能开始生效
@Profile("dev")
public class MainConfigOfProfile implements EmbeddedValueResolverAware {

    private StringValueResolver stringValueResolver;

    @Value("${db.user}")
    private String user;

    private String driverClass;

    @Profile("test")
    @Bean("testDataSource")
    public DataSource dataSourceTest(@Value("${db.password}") String password) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Profile("dev")
    @Bean("devDataSource")
    public DataSource dataSourceDev(@Value("${db.password}") String password) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/shop");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Profile("pro")
    @Bean("proDataSource")
    public DataSource dataSourcePro(@Value("${db.password}") String password) throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser(user);
        dataSource.setPassword(password);
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/pro");
        dataSource.setDriverClass(driverClass);
        return dataSource;
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
        this.driverClass = stringValueResolver.resolveStringValue("db.dirverClass");
    }
}
