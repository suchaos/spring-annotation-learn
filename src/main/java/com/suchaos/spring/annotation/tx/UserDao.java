package com.suchaos.spring.annotation.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Random;
import java.util.UUID;

/**
 * 演示事务
 *
 * @author suchao
 * @date 2019/11/1
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert() {
        String sql = "insert into user(name, age) values(?, ?)";
        String name = UUID.randomUUID().toString().substring(0, 5);
        int age = new Random().nextInt(100);
        jdbcTemplate.update(sql, name, age);
    }
}
