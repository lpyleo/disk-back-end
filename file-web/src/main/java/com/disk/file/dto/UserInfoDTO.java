package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "查询用户信息DTO",required = true)
public class UserInfoDTO {
    @Schema(description="用户名称")
    private String userName;
    @Schema(description = "手机号码")
    private String telephone;
    @Schema(description="用户邮箱")
    private String email;
    @Schema(description="所属部门")
    private String department;
    @Schema(description="所属角色")
    private String role;
    @Schema(description = "性别")
    private String sex;
}
