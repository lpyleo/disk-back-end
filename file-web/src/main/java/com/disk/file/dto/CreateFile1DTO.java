package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "创建文件DTO",required = true)
public class CreateFile1DTO {
    @Schema(description="文件名")
    private String fileName;
    @Schema(description="文件路径")
    private String filePath;
    @Schema(description = "部门Id")
    private Long deptId;
}
