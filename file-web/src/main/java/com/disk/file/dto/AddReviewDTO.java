package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;

@Schema(description = "添加评论DTO")
@Data
public class AddReviewDTO {
    @Schema(description = "评论人Id")
    private Long userId;
    @Schema(description = "评论文件Id")
    private Long fileId;
    @Schema(description = "评论内容")
    private String content;
    @Schema(description = "评论时间")
    private String reviewTime;
}
