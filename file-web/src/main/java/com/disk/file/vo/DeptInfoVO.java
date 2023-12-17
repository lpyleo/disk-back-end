package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="部门信息VO")
@Data
public class DeptInfoVO {
    @Schema(description="部门Id")
    private Long deptId;
    @Schema(description="父级部门Id")
    private Long parentId;
    @Schema(description="部门名称")
    private String deptName;
    @Schema(description="父级部门名称")
    private String parentName;
    @Schema(description="部门邮箱")
    private String email;
    @Schema(description="部门电话")
    private String phone;
}
