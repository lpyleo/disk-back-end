package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="管理员登录VO")
@Data
public class AdminLoginVO {
    @Schema(description="用户名")
    private String adminName;
    @Schema(description="token")
    private String token;
}
