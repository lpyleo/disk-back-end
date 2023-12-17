package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "搜索文件DTO",required = true)
public class SearchFileDTO {
    @Schema(description="文件名", required=true)
    private String fileName;
    @Schema(description = "文件类型")
    private String fileType;
    @Schema(description="当前页", required=true)
    private long currentPage;
    @Schema(description="每页数量", required=true)
    private long pageCount;
}
