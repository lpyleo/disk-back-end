package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.Department;
import com.disk.file.vo.AddDeptVO;
import com.disk.file.vo.DepartmentTreeVO;
import com.disk.file.vo.DeptInfoVO;

import java.util.List;

public interface DepartmentMapper extends BaseMapper<Department> {
    List<DepartmentTreeVO> getDeptTree();
    List<DeptInfoVO> getDeptInfo(Long beginCount, Long pageCount);
    Long getDeptNum();
    List<DeptInfoVO> searchDeptInfo(String deptName, Long beginCount, Long pageCount);
    Long searchNum(String deptName);
    void deleteDept(Long deptId);
    void addDept(Department department);
    int findRank(Long deptId);
    void updateDept(Department department);
    AddDeptVO getDeptInfoById(Long deptId);
    List<DepartmentTreeVO> getAllSonDeptsById(Long deptId);
}
