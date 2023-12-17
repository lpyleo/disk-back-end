package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="部门树VO")
@Data
public class DepartmentTreeVO {
    @Schema(description="部门ID")
    private Long id;
    @Schema(description="父级部门ID")
    private Long parentId;
    @Schema(description="部门名称")
    private String name;
    @Schema(description="部门级别")
    private Integer rank;
}
