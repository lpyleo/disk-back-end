package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.Administrator;
import com.disk.file.model.User;
import com.disk.file.vo.AdminInfoVO;
import com.disk.file.vo.UserInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper extends BaseMapper<Administrator> {
    void insertAdmin(Administrator administrator);
    List<User> selectAdmin();
    AdminInfoVO getAdminInfo(@Param("adminId") Long adminId);
}
