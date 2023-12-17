package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "发布通知DTO")
@Data
public class NoticePublishDTO {
    @Schema(description = "通知标题")
    private String noticeName;
    @Schema(description = "通知内容")
    private String noticeContent;
}
