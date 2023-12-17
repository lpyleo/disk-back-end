package com.disk.file.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "RecoveryDeptFile")
@Entity
@TableName("RecoveryDeptFile")
public class RecoveryDeptFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition="bigint(20)")
    private Long recoveryDeptFileId;
    @Column(columnDefinition = "bigint(20)")
    private Long deptFileId;
    @Column(columnDefinition="varchar(25)")
    private String deleteTime;
    @Column(columnDefinition = "varchar(50)")
    private String deleteBatchNum;
}
