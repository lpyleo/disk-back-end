package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "添加部门DTO")
@Data
public class AddDeptVO {
    @Schema(description = "部门")
    private String deptName;
    @Schema(description = "父级部门Id")
    private Long parentId;
    @Schema(description = "电话")
    private String phone;
    @Schema(description = "邮箱")
    private String email;
}