package com.disk.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disk.file.common.RestResult;
import com.disk.file.model.Company;
import com.disk.file.vo.CompanyInfoVO;

public interface CompanyService extends IService<Company> {
    CompanyInfoVO searchCompanyInfo();
    RestResult<String> updateCompanyInfo(Company company);
}
