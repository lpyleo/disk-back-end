package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="各分公司每日登录人数VO")
@Data
public class LoginTodayCountVO {
    @Schema(description="部门id")
    private Long deptId;
    @Schema(description="部门名称")
    private String deptName;
    @Schema(description = "当日登录人数")
    private int loginCount;
}
