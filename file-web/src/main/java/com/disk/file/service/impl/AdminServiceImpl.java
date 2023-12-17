package com.disk.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disk.file.common.RestResult;
import com.disk.file.mapper.AdminMapper;
import com.disk.file.model.Administrator;
import com.disk.file.service.AdminService;
import com.disk.file.util.DateUtil;
import com.disk.file.util.JwtUtil;
import com.disk.file.vo.AdminInfoVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Administrator> implements AdminService {
    @Resource
    JwtUtil jwtUtil;

    @Resource
    AdminMapper adminMapper;

    @Override
    public RestResult<String> registerAdmin(Administrator administrator){
        //判断验证码
        String adminName = administrator.getAdminName();
        String telephone = administrator.getTelephone();
        String password = administrator.getPassword();
        String avatar = administrator.getAvatar();

        if(!StringUtils.hasLength(adminName)){
            return RestResult.fail().message("用户名不能为空！");
        }
        if (!StringUtils.hasLength(telephone) || !StringUtils.hasLength(password)){
            return RestResult.fail().message("手机号或密码不能为空！");
        }
        if (isTelePhoneExit(telephone)){
            return RestResult.fail().message("手机号已存在！");
        }


        String salt = UUID.randomUUID().toString().replace("-", "").substring(15);
        String passwordAndSalt = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());

        administrator.setSalt(salt);

        administrator.setPassword(newPassword);
        administrator.setRegisterTime(DateUtil.getCurrentTime());
        int result = adminMapper.insert(administrator);

        if (result == 1) {
            return RestResult.success();
        } else {
            return RestResult.fail().message("注册用户失败，请检查输入信息！");
        }
    }

    private boolean isTelePhoneExit(String telePhone) {
        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getTelephone, telePhone);
        List<Administrator> list = adminMapper.selectList(lambdaQueryWrapper);
        if (list != null && !list.isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public RestResult<Administrator> login(Administrator administrator){
        String telephone = administrator.getTelephone();
        String password = administrator.getPassword();

        LambdaQueryWrapper<Administrator> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Administrator::getTelephone, telephone);
        Administrator saveAdmin = adminMapper.selectOne(lambdaQueryWrapper);
        String salt = saveAdmin.getSalt();
        String passwordAndSalt = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());
        if (newPassword.equals(saveAdmin.getPassword())) {
            saveAdmin.setPassword("");
            saveAdmin.setSalt("");
            return RestResult.success().data(saveAdmin);
        } else {
            return RestResult.fail().message("手机号或密码错误！");
        }
    }

    @Override
    public Administrator getAdminByToken(String token) {
        Administrator tokenAdminInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenAdminInfo = objectMapper.readValue(subject, Administrator.class);

        } catch (Exception e) {
            log.error("解码异常");
            return null;

        }
        return tokenAdminInfo;
    }

    @Override
    public AdminInfoVO selectAdminById(Long adminId){
        return adminMapper.getAdminInfo(adminId);
    }
}
