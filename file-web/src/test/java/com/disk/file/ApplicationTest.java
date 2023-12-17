package com.disk.file;

import com.disk.file.mapper.UserMapper;
import com.disk.file.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ApplicationTest {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Test
    public void test1() {
        User user = new User();
        user.setUsername("用户名1");
        user.setPassword("密码1");
        user.setTelephone("手机号1");
        userMapper.insertUser(user);
        System.out.println("数据库字段查询结果显示");
        List<User> list = userMapper.selectUser();
        list.forEach(System.out::println);
    }

    @Test
    public void test2() {
        User user = new User();
        user.setUsername("用户名2");
        user.setPassword("密码2");
        user.setTelephone("手机号2");
        userMapper.insert(user);
        List<User> list = userMapper.selectList(null);
        System.out.println("数据库字段查询结果显示");
        list.forEach(System.out::println);
    }

}
