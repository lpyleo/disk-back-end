package com.disk.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disk.file.common.RestResult;
import com.disk.file.model.User;
import com.disk.file.vo.LoginTodayCountVO;
import com.disk.file.vo.UserDeptInfoVO;
import com.disk.file.vo.UserInfoVO;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    RestResult<String> registerUser(User user);
    RestResult<User> login(User user);
    User getUserByToken(String token);
    UserInfoVO selectUserById(Long userId);
    RestResult<String> updateUser(User user);
    RestResult<String> updatePassword(User user, String newPsd);
    RestResult<List<LoginTodayCountVO>> getLoginTodayCount();
    List<UserDeptInfoVO> getUserDeptInfo(Long currentPage, Long pageCount);
    List<UserDeptInfoVO> getUserDeptInfoByDeptId(Long deptId, Long currentPage, Long pageCount);
    Long getUserDeptCount();
    Long getUserDeptCountByDeptId(Long deptId);
    Map<String, Object> searchUserDept(String username, Long currentPage, Long pageCount);
    Map<String, Object> searchUserDeptByDeptId(Long deptId, Long currentPage, Long pageCount);
    Map<String, Object> deleteUser(String tele, Long currentPage, Long pageCount);
    Map<String, Object> deleteUserBatch(List<String> userIdList, Long currentPage, Long pageCount);
    Map<String, Object> updateUserDept(Long userId, Long deptId, Long currentPage, Long pageCount);
}