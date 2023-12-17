package com.disk.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disk.file.common.RestResult;
import com.disk.file.dto.AddDeptDTO;
import com.disk.file.dto.UpdateDeptDTO;
import com.disk.file.mapper.DepartmentMapper;
import com.disk.file.model.Department;
import com.disk.file.service.DepartmentService;
import com.disk.file.util.JwtUtil;
import com.disk.file.vo.AddDeptVO;
import com.disk.file.vo.DepartmentTreeVO;
import com.disk.file.vo.DeptInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {
    @Resource
    DepartmentMapper departmentMapper;

    @Resource
    JwtUtil jwtUtil;

    @Override
    public List<DepartmentTreeVO> getDeptTree() {
        return departmentMapper.getDeptTree();
    }

    @Override
    public Map<String, Object> getDeptInfo(Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        Map<String, Object> map = new HashMap<>();
        List<DeptInfoVO> list = departmentMapper.getDeptInfo(beginCount, pageCount);
        Long total = departmentMapper.getDeptNum();
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public Map<String, Object> searchDeptInfo(String deptName, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        Map<String, Object> map = new HashMap<>();
        List<DeptInfoVO> list = departmentMapper.searchDeptInfo(deptName, beginCount, pageCount);
        Long total = departmentMapper.searchNum(deptName);
        map.put("list", list);
        map.put("total", total);
        return map;
    }

    @Override
    public void deleteDept(Long deptId){
        departmentMapper.deleteDept(deptId);
    }

    @Override
    public void deleteDeptLists(List<Long> deptId){
        for(int i = 0; i < deptId.size(); i++){
            departmentMapper.deleteDept(deptId.get(i));
        }
    }

    @Override
    public void addDept(AddDeptDTO addDeptDTO){
        Department department = new Department();
        department.setDeptName(addDeptDTO.getDeptName());
        department.setDeptRank(departmentMapper.findRank(addDeptDTO.getParentId()) + 1);
        department.setEmail(addDeptDTO.getEmail());
        department.setPhone(addDeptDTO.getPhone());
        department.setDelFlag("0");
        department.setParentId(addDeptDTO.getParentId());
        departmentMapper.addDept(department);
    }

    @Override
    public void updateDept(UpdateDeptDTO updateDeptDTO){
        Department department = new Department();
        department.setDeptName(updateDeptDTO.getDeptName());
        department.setDeptRank(departmentMapper.findRank(updateDeptDTO.getParentId()) + 1);
        department.setEmail(updateDeptDTO.getEmail());
        department.setPhone(updateDeptDTO.getPhone());
        department.setParentId(updateDeptDTO.getParentId());
        department.setDeptId(updateDeptDTO.getDeptId());
        System.out.println(department);
        departmentMapper.updateDept(department);
    }

    @Override
    public AddDeptVO getDeptInfoById(Long deptId){
        return  departmentMapper.getDeptInfoById(deptId);
    }

    @Override
    public List<DepartmentTreeVO> getAllSonDeptsById(Long deptId){
        return departmentMapper.getAllSonDeptsById(deptId);
    }
}
