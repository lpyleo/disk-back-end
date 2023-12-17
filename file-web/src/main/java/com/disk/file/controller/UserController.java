package com.disk.file.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.disk.file.common.RestResult;

import com.disk.file.dto.RegisterDTO;
import com.disk.file.dto.UpdatePasswordDTO;
import com.disk.file.dto.UserDeptInfoDTO;
import com.disk.file.dto.UserInfoDTO;
import com.disk.file.model.User;
import com.disk.file.service.UserService;
import com.disk.file.util.JwtUtil;
import com.disk.file.vo.LoginTodayCountVO;
import com.disk.file.vo.LoginVO;
import com.disk.file.vo.UserDeptInfoVO;
import com.disk.file.vo.UserInfoVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "user", description = "该接口为用户接口，主要做用户登录，注册和校验token")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    JwtUtil jwtUtil;

    @Operation(summary = "用户注册", description = "注册账号", tags = {"user"})
    @PostMapping(value = "/register")
    @ResponseBody
    public RestResult<String> register(@RequestBody RegisterDTO registerDTO) {
        RestResult<String> restResult = null;
        User user = new User();
        user.setDeptId(registerDTO.getDeptId());
        user.setUsername(registerDTO.getUsername());
        user.setTelephone(registerDTO.getTelephone());
        user.setPassword(registerDTO.getPassword());
        user.setAvatar((registerDTO.getAvatar()));
        user.setRole("普通员工");

        restResult = userService.registerUser(user);

        return restResult;
    }

    @Operation(summary = "用户登录", description = "用户登录认证后才能进入系统", tags = {"user"})
    @GetMapping(value = "/login")
    @ResponseBody
    public RestResult<LoginVO> userLogin(String telephone, String password) {
        RestResult<LoginVO> restResult = new RestResult<LoginVO>();
        LoginVO loginVO = new LoginVO();
        User user = new User();
        user.setTelephone(telephone);
        user.setPassword(password);
        RestResult<User> loginResult = userService.login(user);

        if (!loginResult.getSuccess()) {
            return RestResult.fail().message("登录失败！");
        }

        loginVO.setUsername(loginResult.getData().getUsername());
        String jwt = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jwt = jwtUtil.createJWT(objectMapper.writeValueAsString(loginResult.getData()));
        } catch (Exception e) {
            return RestResult.fail().message("登录失败！");
        }
        loginVO.setToken(jwt);

        return RestResult.success().data(loginVO);
    }

    @Operation(summary = "检查用户登录信息", description = "验证token的有效性", tags = {"user"})
    @GetMapping("/checkuserlogininfo")
    @ResponseBody
    public RestResult<User> checkToken(@RequestHeader("token") String token) {
        RestResult<User> restResult = new RestResult<User>();
        User tokenUserInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);

        } catch (Exception e) {
            log.error("解码异常");
            return RestResult.fail().message("认证失败");

        }

        if (tokenUserInfo != null) {

            return RestResult.success().data(tokenUserInfo);

        } else {
            return RestResult.fail().message("用户暂未登录");
        }
    }

    @Operation(summary = "获取用户信息", description = "根据用户的id获取用户详细信息", tags = {"user"})
    @GetMapping(value = "/getuserinfo")
    @ResponseBody
    public RestResult<UserInfoVO> getUserInfo(@RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }
        long userId = sessionUser.getUserId();

        UserInfoVO userInfo = userService.selectUserById(userId);
        return RestResult.success().data(userInfo);
    }

    @Operation(summary = "更新用户信息", description = "根据用户修改的信息对用户的个人信息进行更新", tags = {"user"})
    @PostMapping(value = "/updateuserinfo")
    @ResponseBody
    public RestResult<String> updateUserInfo(@RequestHeader("token") String token, @RequestBody UserInfoDTO userInfoDTO) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }
        long userId = sessionUser.getUserId();
        sessionUser.setUsername(userInfoDTO.getUserName());
        sessionUser.setTelephone(userInfoDTO.getTelephone());
        sessionUser.setEmail(userInfoDTO.getEmail());
        sessionUser.setSex(userInfoDTO.getSex());

        RestResult<String> restResult = null;
        restResult = userService.updateUser(sessionUser);
        return restResult;
    }


    @Operation(summary = "修改密码", description = "修改用户密码", tags = {"user"})
    @PostMapping(value = "/updatepassword")
    @ResponseBody
    public RestResult<String> updatePassword(@RequestHeader("token") String token, @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }
        long userId = sessionUser.getUserId();
        sessionUser.setPassword(updatePasswordDTO.getOldPassword());
        String newPsd = updatePasswordDTO.getNewPassword();

        RestResult<String> restResult = null;
        restResult = userService.updatePassword(sessionUser, newPsd);
        return restResult;
    }

    @Operation(summary = "各分公司当日登录情况", description = "查询各分公司当日登录情况", tags = {"user"})
    @GetMapping(value = "/getlogintodaycount")
    @ResponseBody
    public RestResult<List<LoginTodayCountVO>> getLoginTodayCount(){
        return userService.getLoginTodayCount();
    }

    @Operation(summary = "用户部门信息", description = "查询用户对应的部门", tags = {"user"})
    @GetMapping(value = "/getuserdeptinfo")
    @ResponseBody
    public RestResult<List<UserDeptInfoVO>> getUserDeptInfo(UserDeptInfoDTO userDeptInfoDTO){
        List<UserDeptInfoVO> userDeptInfoVOList = userService.getUserDeptInfo(userDeptInfoDTO.getCurrentPage(), userDeptInfoDTO.getPageCount());

        Long total = userService.getUserDeptCount();

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", userDeptInfoVOList);

        return RestResult.success().data(map);
    }

    @Operation(summary = "用户部门搜索", description = "模糊搜索用户", tags = {"user"})
    @GetMapping(value = "/searchuserdept")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> searchUserDept(String username, Long currentPage, Long pageCount) {

        Map<String, Object> map = userService.searchUserDept(username, currentPage, pageCount);
        return RestResult.success().data(map);

    }

    @Operation(summary = "用户部门信息搜索", description = "用于后台展示用户部门信息", tags = {"user"})
    @GetMapping(value = "/searchuserdeptbydeptid")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> searchUserDeptByDeptId(Long deptId, Long currentPage, Long pageCount) {

        Map<String, Object> map = userService.searchUserDeptByDeptId(deptId, currentPage, pageCount);
        return RestResult.success().data(map);

    }

    @Operation(summary = "删除用户", description = "根据用户手机号删除用户", tags = {"user"})
    @GetMapping(value = "/deleteuser")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> deleteUser(String tele, Long currentPage, Long pageCount) {
        Map<String, Object> map = userService.deleteUser(tele, currentPage, pageCount);
        return RestResult.success().data(map);
    }

    @Operation(summary = "批量删除用户", description = "根据用户id批量删除用户", tags = {"user"})
    @GetMapping(value = "/deleteuserbatch")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> deleteUserBatch(String list, Long currentPage, Long pageCount) {

        String[] userArray = list.split(",");
        List<String> userList = new ArrayList<>();

        for (String userStr : userArray) {
            userList.add(userStr);
        }
        Map<String, Object> map = userService.deleteUserBatch(userList, currentPage, pageCount);
        return RestResult.success().data(map);
    }

    @Operation(summary = "用户部门修改", description = "根据用户id修改用户部门", tags = {"user"})
    @GetMapping(value = "/updateuserdept")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> updateUserDept(Long userId, Long deptId, Long currentPage, Long pageCount) {

        Map<String, Object> map = userService.updateUserDept(userId, deptId, currentPage, pageCount);
        return RestResult.success().data(map);
    }
}
