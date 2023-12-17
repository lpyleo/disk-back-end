package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "删除部门文件DTO",required = true)
public class DeleteDeptFileDTO {
    @Schema(description = "部门文件id")
    private Long deptFileId;
    @Schema(description = "文件路径")
    @Deprecated
    private String filePath;
    @Schema(description = "文件名")
    @Deprecated
    private String fileName;
    @Schema(description = "是否是目录")
    @Deprecated
    private Integer isDir;
}
