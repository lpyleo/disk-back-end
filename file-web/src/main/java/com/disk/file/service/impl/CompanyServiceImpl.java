package com.disk.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disk.file.common.RestResult;
import com.disk.file.mapper.CompanyMapper;
import com.disk.file.model.Company;
import com.disk.file.service.CompanyService;
import com.disk.file.vo.CompanyInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {
    @Resource
    CompanyMapper companyMapper;

    @Override
    public CompanyInfoVO searchCompanyInfo(){
        return companyMapper.searchCompanyInfo();
    }

    @Override
    public RestResult<String> updateCompanyInfo(Company company){
        companyMapper.updateCompanyInfo(company);
        return RestResult.success();
    }
}
