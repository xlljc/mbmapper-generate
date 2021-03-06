package com.github.xlljc;

import org.junit.jupiter.api.Test;
//import com.github.xlljc.test.dao.UserDao;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.net.URL;

@SpringBootTest
@MapperScan("org.mbmapper_test.test.dao")
class ApplicationTests {

    //@Autowired
    //UserDao userDao;

    @Test
    void contextLoads() {
        /*System.out.println(userDao.queryTest());
        System.out.println(userDao.queryAll());*/
    }

    @Test
    void test1() {
        //URL url = getClass().getClassLoader().getResource("mapper/UserDao.xml");
        URL url = getClass().getClassLoader().getResource("com/github/xlljc/test/dao/UserDao.class");
        System.out.println(url);
        assert url != null;
        File file = new File(url.getFile());
        System.out.println(file.exists());
    }
}
