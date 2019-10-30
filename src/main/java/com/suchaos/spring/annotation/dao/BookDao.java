package com.suchaos.spring.annotation.dao;

import lombok.*;
import org.springframework.stereotype.Repository;

/**
 * BookDao
 *
 * @author suchao
 * @date 2019/10/29
 */
@Repository
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookDao {

    private String label = "@Autowired";
}
