package com.disk.file.service.impl;

import java.util.*;

import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disk.file.common.RestResult;
import com.disk.file.constant.FileConstant;
import com.disk.file.mapper.UserMapper;
import com.disk.file.model.User;
import com.disk.file.model.UserFile;
import com.disk.file.service.UserService;
import com.disk.file.util.DateUtil;

import com.disk.file.util.JwtUtil;
import com.disk.file.vo.LoginTodayCountVO;
import com.disk.file.vo.UserDeptInfoVO;
import com.disk.file.vo.UserInfoVO;
import com.disk.file.vo.UserfileListVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    JwtUtil jwtUtil;

    @Resource
    UserMapper userMapper;

    @Override
    public RestResult<String> registerUser(User user) {
        //判断验证码
        String username = user.getUsername();
        String telephone = user.getTelephone();
        String password = user.getPassword();
        String avatar = user.getAvatar();

        if (!StringUtils.hasLength(username)){
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

        user.setSalt(salt);

        user.setPassword(newPassword);
        user.setRegisterTime(DateUtil.getCurrentTime());
        int result = userMapper.insert(user);

        if (result == 1) {
            return RestResult.success();
        } else {
            return RestResult.fail().message("注册用户失败，请检查输入信息！");
        }
    }

    private boolean isTelePhoneExit(String telePhone) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getTelephone, telePhone);
        List<User> list = userMapper.selectList(lambdaQueryWrapper);
        if (list != null && !list.isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public RestResult<User> login(User user) {
        String telephone = user.getTelephone();
        String password = user.getPassword();

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getTelephone, telephone);
        User saveUser = userMapper.selectOne(lambdaQueryWrapper);
        String salt = saveUser.getSalt();
        String passwordAndSalt = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());
        if (newPassword.equals(saveUser.getPassword())) {
            saveUser.setPassword("");
            saveUser.setSalt("");
            userMapper.updateLastLoginTime(user.getTelephone(), DateUtil.getCurrentTime());
            return RestResult.success().data(saveUser);
        } else {
            return RestResult.fail().message("手机号或密码错误！");
        }

    }

    @Override
    public User getUserByToken(String token) {
        User tokenUserInfo = null;
        try {

            Claims c = jwtUtil.parseJWT(token);
            String subject = c.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);

        } catch (Exception e) {
            log.error("解码异常");
            return null;

        }
        return tokenUserInfo;
    }

    @Override
    public UserInfoVO selectUserById(Long userId){
        return userMapper.getUserInfo(userId);
    }

    @Override
    public RestResult<String> updateUser(User user){
        userMapper.updateUser(user);
        return RestResult.success().data("用户信息修改成功");
    }

    @Override
    public RestResult<String> updatePassword(User user, String newPsd) {
        String password = user.getPassword();
        Long userId = user.getUserId();

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserId, userId);
        User saveUser = userMapper.selectOne(lambdaQueryWrapper);
        String salt = saveUser.getSalt();
        String passwordAndSalt = password + salt;
        String newPassword = DigestUtils.md5DigestAsHex(passwordAndSalt.getBytes());
        if (newPassword.equals(saveUser.getPassword())) {
            user.setPassword(DigestUtils.md5DigestAsHex((newPsd+salt).getBytes()));
            userMapper.updatePassword(user);
            return RestResult.success().data("用户密码修改成功");
        } else {
            return RestResult.fail().message("原密码输入错误");
        }
    }

    @Override
    public RestResult<List<LoginTodayCountVO>> getLoginTodayCount() {
        return RestResult.success().data(userMapper.getLoginTodayCount());
    }

    @Override
    public List<UserDeptInfoVO> getUserDeptInfo(Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        return userMapper.getUserDeptInfo("", beginCount, pageCount);
    }

    @Override
    public List<UserDeptInfoVO> getUserDeptInfoByDeptId(Long deptId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        return userMapper.getUserDeptInfoByDeptId(deptId, beginCount, pageCount);
    }

    @Override
    public Long getUserDeptCount(){
        return userMapper.getUserDeptCount("");
    }

    @Override
    public Long getUserDeptCountByDeptId(Long deptId){
        return userMapper.getUserDeptCountByDeptId(deptId);
    }

    @Override
    public Map<String, Object> searchUserDept(String username, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserDeptInfoVO> list;
        Long total;

        User user = new User();
        user.setUsername(username);
        list = userMapper.getUserDeptInfo(username, beginCount, pageCount);
        total = userMapper.getUserDeptCount(username);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> searchUserDeptByDeptId(Long deptId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserDeptInfoVO> list;
        Long total;


        list = userMapper.getUserDeptInfoByDeptId(deptId, beginCount, pageCount);
        total = userMapper.getUserDeptCountByDeptId(deptId);
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> deleteUser(String tele, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserDeptInfoVO> list;
        Long total;

        userMapper.deleteUserByTele(tele);
        list = userMapper.getUserDeptInfo("", beginCount, pageCount);
        total = userMapper.getUserDeptCount("");
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> deleteUserBatch(List<String> userList, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserDeptInfoVO> list;
        Long total;

        for(int i = 0; i < userList.size(); i++){
            userMapper.deleteUserByTele(userList.get(i));
        }

        list = userMapper.getUserDeptInfo("", beginCount, pageCount);
        total = userMapper.getUserDeptCount("");
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> updateUserDept(Long userId, Long deptId, Long currentPage, Long pageCount){
        Long beginCount = (currentPage - 1) * pageCount;
        List<UserDeptInfoVO> list;
        Long total;

        userMapper.updateUserDept(userId, deptId);
        list = userMapper.getUserDeptInfo("", beginCount, pageCount);
        total = userMapper.getUserDeptCount("");
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return map;
    }
}