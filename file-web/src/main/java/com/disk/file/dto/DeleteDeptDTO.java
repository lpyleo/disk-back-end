package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "删除部门DTO")
@Data
public class DeleteDeptDTO {
    @Schema(description = "部门Id")
    private Long deptId;
}
