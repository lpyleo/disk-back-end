package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "系统通知信息VO")
@Data
public class NoticeInfoVO {
    @Schema(description = "通知Id")
    private Long noticeId;
    @Schema(description = "通知标题")
    private String noticeName;
    @Schema(description = "通知内容")
    private String noticeContent;
    @Schema(description = "通知时间")
    private String sendTime;
    @Schema(description = "通知发送人")
    private String sender;
    @Schema(description = "更新时间")
    private String updateTime;
}
