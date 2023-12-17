package com.disk.file.controller;

import com.disk.file.common.RestResult;
import com.disk.file.dto.AdminRegisterDTO;
import com.disk.file.model.Administrator;
import com.disk.file.model.User;
import com.disk.file.service.AdminService;
import com.disk.file.util.JwtUtil;
import com.disk.file.vo.AdminInfoVO;
import com.disk.file.vo.AdminLoginVO;
import com.disk.file.vo.UserInfoVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "admin", description = "该接口为管理员接口，主要做管理员登录，注册和校验token")
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    @Resource
    JwtUtil jwtUtil;

    @Operation(summary = "管理员注册", description = "注册账号", tags = {"admin"})
    @PostMapping(value = "/register")
    @ResponseBody
    public RestResult<String> register(@RequestBody AdminRegisterDTO adminRegisterDTO) {
        RestResult<String> restResult = null;
        Administrator administrator = new Administrator();
        administrator.setAdminName(adminRegisterDTO.getAdminName());
        administrator.setTelephone(adminRegisterDTO.getTelephone());
        administrator.setPassword(adminRegisterDTO.getPassword());
        administrator.setAvatar((adminRegisterDTO.getAvatar()));

        restResult = adminService.registerAdmin(administrator);

        return restResult;
    }

    @Operation(summary = "管理员登录", description = "管理员登录认证后才能进入系统", tags = {"admin"})
    @GetMapping(value = "/login")
    @ResponseBody
    public RestResult<AdminLoginVO> adminLogin(String telephone, String password) {
        RestResult<AdminLoginVO> restResult = new RestResult<AdminLoginVO>();
        AdminLoginVO adminLoginVO = new AdminLoginVO();
        Administrator administrator = new Administrator();
        administrator.setTelephone(telephone);
        administrator.setPassword(password);
        RestResult<Administrator> loginResult = adminService.login(administrator);

        if (!loginResult.getSuccess()) {
            return RestResult.fail().message("登录失败！");
        }

        adminLoginVO.setAdminName(loginResult.getData().getAdminName());
        String jwt = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jwt = jwtUtil.createJWT(objectMapper.writeValueAsString(loginResult.getData()));
        } catch (Exception e) {
            return RestResult.fail().message("登录失败！");
        }
        adminLoginVO.setToken(jwt);

        return RestResult.success().data(adminLoginVO);
    }

    @Operation(summary = "检查管理员登录信息", description = "验证token的有效性", tags = {"admin"})
    @GetMapping("/checkadminlogininfo")
    @ResponseBody
    public RestResult<Administrator> checkToken(@RequestHeader("token") String token) {
        RestResult<Administrator> restResult = new RestResult<Administrator>();
        Administrator tokenAdminInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenAdminInfo = objectMapper.readValue(subject, Administrator.class);

        } catch (Exception e) {
            log.error("解码异常");
            return RestResult.fail().message("认证失败");

        }

        if (tokenAdminInfo != null) {

            return RestResult.success().data(tokenAdminInfo);

        } else {
            return RestResult.fail().message("用户暂未登录");
        }
    }

    @Operation(summary = "获取管理员信息", description = "根据管理员的id获取管理员详细信息", tags = {"admin"})
    @GetMapping(value = "/getAdminInfo")
    @ResponseBody
    public RestResult<AdminInfoVO> getAdminInfo(@RequestHeader("token") String token) {
        Administrator sessionAdmin = adminService.getAdminByToken(token);
        if (sessionAdmin == null) {
            return RestResult.fail().message("token验证失败");
        }
        long adminId = sessionAdmin.getAdminId();

        AdminInfoVO adminInfo = adminService.selectAdminById(adminId);
        return RestResult.success().data(adminInfo);
    }
}
