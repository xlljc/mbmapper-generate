package org.mbmapper;

import org.junit.jupiter.api.Test;
import org.mbmapper.test.dao.UserDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;

@SpringBootTest
@MapperScan("org.mbmapper.test.dao")
class ApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    void contextLoads() {
        System.out.println(userDao.queryTest());
        System.out.println(userDao.queryAll());
    }

    @Test
    void test1() {
        //URL url = getClass().getClassLoader().getResource("mapper/UserDao.xml");
        URL url = getClass().getClassLoader().getResource("org/mbmapper/test/dao/UserDao.class");
        System.out.println(url);
        assert url != null;
        File file = new File(url.getFile());
        System.out.println(file.exists());
    }
}
