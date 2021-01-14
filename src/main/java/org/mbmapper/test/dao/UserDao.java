package org.mbmapper.test.dao;

import org.mbmapper.test.vo.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserDao {

    /*List<User> queryAll();*/

    List<Map<String,Object>> queryTest();

    List<User> queryAll();

}
