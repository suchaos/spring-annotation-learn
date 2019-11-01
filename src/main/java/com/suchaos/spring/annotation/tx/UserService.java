package com.suchaos.spring.annotation.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 演示事务
 *
 * @author suchao
 * @date 2019/11/1
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional()
    public void insertUser() {
        userDao.insert();
        System.out.println("insert 完成");

        //int i = 1 /0;
    }
}
