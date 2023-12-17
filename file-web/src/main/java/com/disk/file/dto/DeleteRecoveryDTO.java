package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "删除回收站文件DTO",required = true)
public class DeleteRecoveryDTO {
    @Schema(description = "用户文件id")
    private Long userFileId;
}
