package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "搜索系统通知列表DTO",required = true)
public class NoticeInfoListDTO {
    @Schema(description = "通知标题")
    private String noticeName;
    @Schema(description = "当前页码")
    private Long currentPage;
    @Schema(description = "一页显示数量")
    private Long pageCount;
}