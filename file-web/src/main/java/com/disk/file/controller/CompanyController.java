package com.disk.file.controller;

import com.disk.file.common.RestResult;
import com.disk.file.dto.UpdateCompanyInfoDTO;
import com.disk.file.dto.UserInfoDTO;
import com.disk.file.model.Company;
import com.disk.file.model.User;
import com.disk.file.service.CompanyService;
import com.disk.file.vo.CompanyInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "company", description = "该接口为企业接口，主要作用为管理企业信息")
@Slf4j
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Resource
    CompanyService companyService;

    @Operation(summary = "获取企业信息", description = "获取到企业的详细信息", tags = {"company"})
    @GetMapping(value = "/getcompanyInfo")
    @ResponseBody
    public RestResult<CompanyInfoVO> getCompanyInfo() {
        CompanyInfoVO companyInfo = companyService.searchCompanyInfo();
        return RestResult.success().data(companyInfo);
    }

    @Operation(summary = "更新企业信息", description = "根据管理员修改的信息对企业信息进行更新", tags = {"company"})
    @PostMapping(value = "/updatecompanyinfo")
    @ResponseBody
    public RestResult<String> updateCompanyInfo(@RequestBody UpdateCompanyInfoDTO updateCompanyInfoDTO) {
        Company company = new Company();
        company.setCompanyName(updateCompanyInfoDTO.getCompanyName());
        company.setCompanyDetail(updateCompanyInfoDTO.getCompanyDetail());
        RestResult<String> restResult = null;
        restResult = companyService.updateCompanyInfo(company);
        return restResult;
    }
}
