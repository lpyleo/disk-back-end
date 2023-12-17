package com.disk.file.controller;

import com.disk.file.common.RestResult;
import com.disk.file.dto.*;
import com.disk.file.model.Department;
import com.disk.file.model.User;
import com.disk.file.service.DepartmentService;
import com.disk.file.util.JwtUtil;
import com.disk.file.vo.AddDeptVO;
import com.disk.file.vo.DepartmentTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "department", description = "该接口为部门操作的接口")
@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Resource
    DepartmentService departmentService;

    @Resource
    JwtUtil jwtUtil;

    @Operation(summary = "部门树", description = "用于前台显示部门列表", tags = {"department"})
    @GetMapping(value = "/departmenttree")
    @ResponseBody
    public RestResult<DepartmentTreeVO> getDeptTree(){
        return RestResult.success().data(departmentService.getDeptTree());
    }

    @Operation(summary = "查询部门", description = "查询部门列表", tags = {"department"})
    @GetMapping(value = "/getdeptlist")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> getDeptList(Long currentPage, Long pageCount) {

        Map<String, Object> map = departmentService.getDeptInfo(currentPage, pageCount);
        return RestResult.success().data(map);

    }

    @Operation(summary = "模糊查询部门", description = "根据部门名称模糊查询部门列表", tags = {"department"})
    @GetMapping(value = "/searchdeptlist")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> searchDeptList(String deptName, Long currentPage, Long pageCount) {

        Map<String, Object> map = departmentService.searchDeptInfo(deptName, currentPage, pageCount);
        return RestResult.success().data(map);

    }

    @Operation(summary = "删除部门", description = "删除部门", tags = {"department"})
    @RequestMapping(value = "/deletedept", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteDept(@RequestBody DeleteDeptDTO deleteDeptDTO) {

        departmentService.deleteDept(deleteDeptDTO.getDeptId());

        return RestResult.success();

    }

    @Operation(summary = "批量删除部门", description = "批量删除部门", tags = {"department"})
    @RequestMapping(value = "/deletedeptlists", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteDeptLists(@RequestBody BatchDeleteDepts deptList) {

        String[] deptIdArray = deptList.getDeptIds().split(",");
        List<Long> deptIdLongList = new ArrayList<>();

        for (String str : deptIdArray) {
            deptIdLongList.add(Long.parseLong(str));
        }
        departmentService.deleteDeptLists(deptIdLongList);

        return RestResult.success();

    }


    @Operation(summary = "新增部门", description = "新增部门", tags = {"department"})
    @RequestMapping(value = "/adddept", method = RequestMethod.POST)
    @ResponseBody
    public RestResult addDept(@RequestBody AddDeptDTO addDeptDTO) {

        departmentService.addDept(addDeptDTO);

        return RestResult.success();

    }

    @Operation(summary = "更新部门", description = "更新部门信息", tags = {"department"})
    @RequestMapping(value = "/updatedept", method = RequestMethod.POST)
    @ResponseBody
    public RestResult updateDept(@RequestBody UpdateDeptDTO updateDeptDTO) {

        departmentService.updateDept(updateDeptDTO);

        return RestResult.success();

    }

    @Operation(summary = "查询单个部门", description = "根据部门Id查询部门", tags = {"department"})
    @GetMapping(value = "/getdeptbyid")
    @ResponseBody
    public RestResult<AddDeptVO> getDeptById(Long deptId) {

        AddDeptVO addDeptVO = departmentService.getDeptInfoById(deptId);
        return RestResult.success().data(addDeptVO);

    }

    @Operation(summary = "查询子部门", description = "根据部门Id查询该部门的所有子部门", tags = {"department"})
    @GetMapping(value = "/getallsondeptbyid")
    @ResponseBody
    public RestResult<DepartmentTreeVO> getAllSonDeptByid(Long deptId) {

        List<DepartmentTreeVO> departments = departmentService.getAllSonDeptsById(deptId);
        return RestResult.success().data(departments);

    }
}
