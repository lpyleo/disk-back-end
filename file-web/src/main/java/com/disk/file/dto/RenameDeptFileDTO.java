package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "重命名部门文件DTO",required = true)
public class RenameDeptFileDTO {
    @Schema(description = "文件Id")
    private Long deptFileId;

    @Schema(description = "文件名")
    private String fileName;
}
