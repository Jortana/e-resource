package cn.edu.njnu;


import cn.edu.njnu.mapper.UserMapper;
import cn.edu.njnu.pojo.UserNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;


@SpringBootTest
class EResourceApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Test
    UserNode getById() {
        UserNode model = userMapper.getByID(21);
        System.out.println(1);
        System.out.println(model);
        return model;
    }
}
