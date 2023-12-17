package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.Company;
import com.disk.file.vo.CompanyInfoVO;

public interface CompanyMapper extends BaseMapper<Company> {
    CompanyInfoVO searchCompanyInfo();
    void updateCompanyInfo(Company company);
}
