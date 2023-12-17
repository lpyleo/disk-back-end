package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description="用户文件列表VO")
public class UserfileListVO {
    @Schema(description="文件id")
    private Long fileId;
    @Schema(description="时间戳名称")
    private String timeStampName;
    @Schema(description="文件url")
    private String fileUrl;
    @Schema(description="文件大小")
    private Long fileSize;
    @Schema(description="是否是oss存储")
    private Integer isOSS;
    @Schema(description="引用数量")
    private Integer pointCount;
    @Schema(description="md5")
    private String identifier;
    @Schema(description="用户文件id")
    private Long userFileId;
    @Schema(description="用户id")
    private Long userId;

    @Schema(description="文件名")
    private String fileName;
    @Schema(description="文件路径")
    private String filePath;
    @Schema(description="扩展名")
    private String extendName;
    @Schema(description="是否是目录")
    private Integer isDir;
    @Schema(description="上传时间")
    private String uploadTime;
}