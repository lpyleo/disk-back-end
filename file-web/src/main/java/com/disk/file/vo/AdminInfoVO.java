package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询管理员信息VO")
public class AdminInfoVO {
    @Schema(description = "头像地址")
    private String avatar;
    @Schema(description = "用户名称")
    private String adminName;
    @Schema(description = "手机号码")
    private String telephone;
}
