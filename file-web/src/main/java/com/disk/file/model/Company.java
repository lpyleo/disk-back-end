package com.disk.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@TableName("Company")
@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition="bigint(20) comment '企业id'")
    private Long companyId;

    @Column(columnDefinition="varchar(100) comment '企业名'")
    private String companyName;

    @Column(columnDefinition="longtext comment '企业具体信息'")
    private String companyDetail;
}
