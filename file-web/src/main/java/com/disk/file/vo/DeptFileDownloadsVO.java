package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="文件下载量VO")
@Data
public class DeptFileDownloadsVO {
    @Schema(description="文件Id")
    private Long deptFileId;
    @Schema(description="文件名")
    private String fileName;
    @Schema(description="扩展名")
    private String extendName;
    @Schema(description="下载量")
    private Long downloads;
}
