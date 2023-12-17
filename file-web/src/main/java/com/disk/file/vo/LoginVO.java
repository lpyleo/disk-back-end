package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="登录VO")
@Data
public class LoginVO {
    @Schema(description="用户名")
    private String username;
    @Schema(description="token")
    private String token;
}