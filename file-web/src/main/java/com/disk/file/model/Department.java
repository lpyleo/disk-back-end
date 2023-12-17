package com.disk.file.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@TableName("Department")
@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition="bigint(20) comment '部门ID'")
    private Long deptId;

    @Column(columnDefinition="bigint(20) comment '父级部门ID'")
    private Long parentId;

    @Column(columnDefinition = "varchar(30) comment '部门名称'")
    private String deptName;

    @Column(columnDefinition = "int(10) comment '部门级别'")
    private Integer deptRank;

    @Column(columnDefinition = "varchar(20) comment '联系电话'")
    private String phone;

    @Column(columnDefinition = "varchar(35) comment '邮箱'")
    private String email;

    @Column(columnDefinition = "varchar(2) comment '删除标志 0-存在, 2-删除'")
    private String delFlag;
}
