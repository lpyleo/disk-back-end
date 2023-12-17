package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.User;
import com.disk.file.vo.UserDeptInfoVO;
import com.disk.file.vo.UserInfoVO;
import com.disk.file.vo.LoginTodayCountVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    void insertUser(User user);
    List<User> selectUser();
    UserInfoVO getUserInfo(@Param("userId") Long userId);
    void updateUser(User user);
    void updatePassword(User user);
    void updateLastLoginTime(String telephone, String time);
    List<LoginTodayCountVO> getLoginTodayCount();
    List<UserDeptInfoVO> getUserDeptInfo(String username, Long beginCount, Long pageCount);
    List<UserDeptInfoVO> getUserDeptInfoByDeptId(Long deptId, Long beginCount, Long pageCount);
    Long getUserDeptCount(String username);
    Long getUserDeptCountByDeptId(Long deptId);
    void deleteUser(Long userId);
    void deleteUserByTele(String tele);
    void updateUserDept(Long userId, Long deptId);
}
