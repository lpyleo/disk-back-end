package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "批量删除部门DTO",required = true)
public class BatchDeleteDepts {
    @Schema(description="部门Id集合")
    private String deptIds;
}
