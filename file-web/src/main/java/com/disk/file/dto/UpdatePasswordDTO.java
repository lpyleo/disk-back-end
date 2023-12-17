package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "修改密码DTO",required = true)
public class UpdatePasswordDTO {
    @Schema(description="原密码", required=true)
    private String oldPassword;
    @Schema(description="新密码", required=true)
    private String newPassword;
}
