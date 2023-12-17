package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "删除回收站部门文件DTO",required = true)
public class DeleteDeptRecoveryDTO {
    @Schema(description = "部门文件id")
    private Long deptFileId;
}
