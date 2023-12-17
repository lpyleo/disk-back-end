package com.disk.file.controller;

import com.disk.file.common.RestResult;
import com.disk.file.dto.DownloadDeptFileDTO;
import com.disk.file.dto.DownloadFileDTO;
import com.disk.file.dto.UploadFileDTO;
import com.disk.file.model.*;
import com.disk.file.service.*;
import com.disk.file.util.DateUtil;
import com.disk.file.util.FileUtil;
import com.disk.file.vo.UploadFileVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "deptfiletransfer", description = "该接口为部门文件传输接口，主要用来做部门文件的上传和下载")
@RestController
@RequestMapping("/deptfiletransfer")
public class DeptFiletransferController {
    @Resource
    UserService userService;
    @Resource
    FileService fileService;
    @Resource
    DepartmentfileService departmentfileService;
    @Resource
    DeptFiletransferService deptFiletransferService;

    @Operation(summary = "极速上传", description = "校验文件MD5判断文件是否存在，如果存在直接上传成功并返回skipUpload=true，如果不存在返回skipUpload=false需要再次调用该接口的POST方法", tags = {"deptfiletransfer"})
    @GetMapping(value = "/uploadfile")
    @ResponseBody
    public RestResult<UploadFileVo> uploadFileSpeed(UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null){

            return RestResult.fail().message("未登录");
        }

        UploadFileVo uploadFileVo = new UploadFileVo();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("identifier", uploadFileDto.getIdentifier());
        synchronized (FiletransferController.class) {
            List<File> list = fileService.listByMap(param);
            if (list != null && !list.isEmpty()) {
                File file = list.get(0);

                DepartmentFile departmentFile = new DepartmentFile();
                departmentFile.setFileId(file.getFileId());
                departmentFile.setDeptId(sessionUser.getDeptId());
                departmentFile.setFilePath(uploadFileDto.getFilePath());
                String fileName = uploadFileDto.getFilename();
                departmentFile.setFileName(fileName.substring(0, fileName.lastIndexOf(".")));
                departmentFile.setExtendName(FileUtil.getFileExtendName(fileName));
                departmentFile.setIsDir(0);
                departmentFile.setUploadTime(DateUtil.getCurrentTime());
                departmentFile.setDeleteFlag(0);
                departmentfileService.save(departmentFile);
                uploadFileVo.setSkipUpload(true);

            } else {
                uploadFileVo.setSkipUpload(false);

            }
        }
        return RestResult.success().data(uploadFileVo);

    }

    @Operation(summary = "上传文件", description = "真正的上传文件接口", tags = {"deptfiletransfer"})
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<UploadFileVo> uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null){
            return RestResult.fail().message("未登录");
        }


        deptFiletransferService.uploadFile(request, uploadFileDto, sessionUser.getDeptId());
        UploadFileVo uploadFileVo = new UploadFileVo();
        return RestResult.success().data(uploadFileVo);

    }

    @Operation(summary = "下载文件", description = "下载文件接口", tags = {"deptfiletransfer"})
    @RequestMapping(value = "/downloadfile", method = RequestMethod.GET)
    public void downloadFile(HttpServletResponse response, DownloadDeptFileDTO downloadDeptFileDTO) {
        deptFiletransferService.downloadFile(response, downloadDeptFileDTO);
    }

    @Operation(summary = "获取存储信息", description = "获取存储信息", tags = {"deptfiletransfer"})
    @RequestMapping(value = "/getstorage", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<Long> getStorage(@RequestHeader("token") String token) {

        User sessionUserBean = userService.getUserByToken(token);
        Storage storageBean = new Storage();


        Long storageSize = deptFiletransferService.selectStorageSizeByDeptId(sessionUserBean.getDeptId());
        return RestResult.success().data(storageSize);

    }
}
