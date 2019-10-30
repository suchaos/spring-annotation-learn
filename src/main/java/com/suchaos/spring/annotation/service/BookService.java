package com.suchaos.spring.annotation.service;

import com.suchaos.spring.annotation.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * BookService
 *
 * @author suchao
 * @date 2019/10/29
 */
@Service
public class BookService {

    @Autowired(required = false)
    @Qualifier("bookDao2")
    private BookDao bookDao;

    public void printDao() {
        System.out.println(bookDao);
    }
}
