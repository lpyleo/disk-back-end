package com.disk.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disk.file.common.RestResult;
import com.disk.file.model.Administrator;
import com.disk.file.vo.AdminInfoVO;

public interface AdminService extends IService<Administrator> {
    RestResult<String> registerAdmin(Administrator administrator);
    RestResult<Administrator> login(Administrator administrator);
    Administrator getAdminByToken(String token);
    AdminInfoVO selectAdminById(Long adminId);
}
