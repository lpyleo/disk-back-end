package com.disk.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@TableName("User")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment '用户id'")
    private Long userId;

    @Column(columnDefinition = "varchar(30) comment '用户名'")
    private String username;

    @Column(columnDefinition = "varchar(35) comment '密码'")
    private String password;

    @Column(columnDefinition = "varchar(15) comment '手机号码'")
    private String telephone;

    @Column(columnDefinition = "varchar(20) comment '盐值'")
    private String salt;

    @Column(columnDefinition = "varchar(30) comment '注册时间'")
    private String registerTime;

    @Column(columnDefinition = "varchar(2) comment '性别'")
    private String sex;

    @Column(columnDefinition = "varchar(35) comment '邮箱'")
    private String email;

    @Column(columnDefinition = "varchar(10) comment '部门id'")
    private Long deptId;

    @Column(columnDefinition = "varchar(10) comment '角色'")
    private String role;

    @Column(columnDefinition = "varchar(100) comment '头像'")
    private String avatar;

    @Column(columnDefinition = "varchar(30) comment '最近登录时间")
    private String lastLoginTime;

    @Column(columnDefinition = "varchar(2) comment '删除标志 0-未删除 1-已删除 default '0'")
    private String deleteFlag;
}
