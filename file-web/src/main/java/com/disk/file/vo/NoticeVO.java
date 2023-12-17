package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description="系统通知VO")
@Data
public class NoticeVO {
    @Schema(description="通知标题")
    private String noticeName;
    @Schema(description="通知内容")
    private String noticeContent;
    @Schema(description="通知时间")
    private String sendTime;
}
