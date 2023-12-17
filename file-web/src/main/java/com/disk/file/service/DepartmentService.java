package com.disk.file.service;

import com.disk.file.dto.AddDeptDTO;
import com.disk.file.dto.UpdateDeptDTO;
import com.disk.file.model.Department;
import com.disk.file.vo.AddDeptVO;
import com.disk.file.vo.DepartmentTreeVO;
import com.disk.file.vo.DeptInfoVO;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    List<DepartmentTreeVO> getDeptTree();
    Map<String, Object> getDeptInfo(Long currentPage, Long pageCount);
    Map<String, Object> searchDeptInfo(String deptName, Long currentPage, Long pageCount);
    void deleteDept(Long deptId);
    void deleteDeptLists(List<Long> deptId);
    void addDept(AddDeptDTO addDeptDTO);
    void updateDept(UpdateDeptDTO updateDeptDTO);
    AddDeptVO getDeptInfoById(Long deptId);
    List<DepartmentTreeVO> getAllSonDeptsById(Long deptId);
}
