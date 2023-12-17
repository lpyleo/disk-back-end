package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理员注册DTO")
@Data
public class AdminRegisterDTO {
    @Schema(description = "用户名")
    private String adminName;
    @Schema(description = "手机号")
    private String telephone;
    @Schema(description = "密码")
    private String password;
    @Schema(description = "头像")
    private String avatar;
}
