package com.disk.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@TableName("Notice")
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment '通知id'")
    private Long noticeId;

    @Column(columnDefinition = "varchar(30) comment '通知标题'")
    private String noticeName;

    @Column(columnDefinition = "varchar(30) comment '发送时间'")
    private String sendTime;

    @Column(columnDefinition = "bigint(20) comment '发送人id'")
    private Long senderId;

    @Column(columnDefinition = "longtext comment '通知内容'")
    private String noticeContent;

    @Column(columnDefinition = "varchar(30) comment '更新时间'")
    private String updateTime;

    @Column(columnDefinition="int(1) comment '删除标志 0-未删除 1-已删除'")
    private Integer deleteFlag;
}
