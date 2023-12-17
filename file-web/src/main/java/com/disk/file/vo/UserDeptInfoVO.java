package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询用户部门信息VO")
public class UserDeptInfoVO {
    @Schema(description = "用户Id")
    private Long userId;
    @Schema(description = "用户名称")
    private String username;
    @Schema(description = "手机号码")
    private String telephone;
    @Schema(description = "部门名称")
    private String deptName;
    @Schema(description = "父级部门名称")
    private String parentDeptName;
}
