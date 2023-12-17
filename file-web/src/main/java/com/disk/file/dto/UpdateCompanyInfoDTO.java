package com.disk.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "更新企业信息DTO",required = true)
public class UpdateCompanyInfoDTO {
    @Schema(description = "企业名")
    private String companyName;
    @Schema(description = "企业具体信息")
    private String companyDetail;
}
