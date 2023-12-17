package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询评论VO")
public class ReviewVO {
    @Schema(description = "评论人名称")
    private String username;
    @Schema(description = "评论人头像")
    private String avatar;
    @Schema(description = "评论内容")
    private String content;
    @Schema(description = "评论时间")
    private String reviewTime;
}
