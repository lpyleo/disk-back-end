package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "下载部门文件DTO",required = true)
public class DownloadDeptFileDTO {
    private Long deptFileId;
}
