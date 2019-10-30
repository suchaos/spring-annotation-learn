package com.suchaos.spring.annotation.controller;

import com.suchaos.spring.annotation.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * BookController
 *
 * @author suchao
 * @date 2019/10/29
 */
@Controller
public class BookController {

    @Autowired
    private BookService bookService;
}
