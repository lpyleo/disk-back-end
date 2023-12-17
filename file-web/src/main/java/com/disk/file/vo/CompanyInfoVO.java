package com.disk.file.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "查询企业信息VO")
public class CompanyInfoVO {
    @Schema(description = "企业名")
    private String companyName;
    @Schema(description = "企业具体信息")
    private String companyDetail;
}
