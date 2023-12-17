package com.disk.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@TableName("Review")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment '评论id'")
    private Long reviewId;
    @Column(columnDefinition = "bigint(20) comment '评论人id'")
    private Long userId;
    @Column(columnDefinition = "bigint(20) comment '评论文件id'")
    private Long fileId;
    @Column(columnDefinition = "varchar(500) comment '评论内容'")
    private String content;
    @Column(columnDefinition= "varchar(25) comment '评论时间'")
    private String reviewTime;
}
