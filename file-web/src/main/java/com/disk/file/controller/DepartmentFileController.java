package com.disk.file.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.disk.file.common.RestResult;
import com.disk.file.dto.*;
import com.disk.file.model.DepartmentFile;
import com.disk.file.model.File;
import com.disk.file.model.User;
import com.disk.file.service.*;
import com.disk.file.util.DateUtil;
import com.disk.file.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@Tag(name = "departmentfile", description = "该接口为部门文件接口，主要用来做一些文件的基本操作，如创建目录，删除，移动，复制等。")
@RestController
@Slf4j
@RequestMapping("/departmentfile")
public class DepartmentFileController {

    @Resource
    FileService fileService;
    @Resource
    UserService userService;
    @Resource
    DepartmentService departmentService;
    @Resource
    DepartmentfileService departmentfileService;

    public TreeNodeVO insertTreeNode(TreeNodeVO treeNode, String filePath, Queue<String> nodeNameQueue) {

        List<TreeNodeVO> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null) {
            return treeNode;
        }

        Map<String, String> map = new HashMap<>();
        filePath = filePath + currentNodeName + "/";
        map.put("filePath", filePath);

        if (!isExistPath(childrenTreeNodes, currentNodeName)) {  //1、判断有没有该子节点，如果没有则插入
            //插入
            TreeNodeVO resultTreeNode = new TreeNodeVO();


            resultTreeNode.setAttributes(map);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            // resultTreeNode.setId(treeid++);

            childrenTreeNodes.add(resultTreeNode);

        } else {  //2、如果有，则跳过
            nodeNameQueue.poll();
        }

        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {

                TreeNodeVO childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())) {
                    childrenTreeNode = insertTreeNode(childrenTreeNode, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }

            }
        } else {
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;

    }

    public boolean isExistPath(List<TreeNodeVO> childrenTreeNodes, String path) {
        boolean isExistPath = false;

        try {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {
                if (path.equals(childrenTreeNodes.get(i).getLabel())) {
                    isExistPath = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return isExistPath;
    }

    @Operation(summary = "创建文件", description = "目录(文件夹)的创建", tags = {"departmentfile"})
    @PostMapping(value = "/createfile")
    @ResponseBody
    public RestResult<String> createFile(@RequestBody CreateFileDTO createFileDto, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");

        }
        LambdaQueryWrapper<DepartmentFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DepartmentFile::getFileName, "").eq(DepartmentFile::getFilePath, "").eq(DepartmentFile::getDeptId, 0);
        List<DepartmentFile> deptfiles = departmentfileService.list(lambdaQueryWrapper);
        if (!deptfiles.isEmpty()) {
            RestResult.fail().message("同目录下文件名重复");
        }

        DepartmentFile deptFile = new DepartmentFile();
        deptFile.setDeptId(sessionUser.getDeptId());
        deptFile.setFileName(createFileDto.getFileName());
        deptFile.setFilePath(createFileDto.getFilePath());
        deptFile.setIsDir(1);
        deptFile.setUploadTime(DateUtil.getCurrentTime());
        deptFile.setDeleteFlag(0);
        departmentfileService.save(deptFile);
        return RestResult.success();
    }

    @Operation(summary = "获取文件列表", description = "用来做前台文件列表展示", tags = {"departmentfile"})
    @GetMapping(value = "/getfilelist")
    @ResponseBody
    public RestResult<DepartmentfileListVO> getDeptfileList(DepartmentFileListDTO departmentFileListDTO,
                                                      @RequestHeader("token") String token) {


        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");

        }

        List<DepartmentfileListVO> fileList = departmentfileService.getDeptFileByFilePath(departmentFileListDTO.getFilePath(),
                sessionUser.getDeptId(), departmentFileListDTO.getCurrentPage(), departmentFileListDTO.getPageCount());
        LambdaQueryWrapper<DepartmentFile> deptFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deptFileLambdaQueryWrapper.eq(DepartmentFile::getDeptId, sessionUser.getDeptId())
                .eq(DepartmentFile::getFilePath, departmentFileListDTO.getFilePath()).eq(DepartmentFile::getDeleteFlag, 0);
        int total = departmentfileService.count(deptFileLambdaQueryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", fileList);

        return RestResult.success().data(map);

    }

    @Operation(summary = "通过文件类型选择文件", description = "该接口可以实现文件格式分类查看", tags = {"departmentfile"})
    @GetMapping(value = "/selectfilebyfiletype")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> selectFileByFileType(int fileType, Long currentPage, Long pageCount, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }
        long deptId = sessionUser.getDeptId();

        Map<String, Object> map = departmentfileService.getDeptFileByType(fileType, currentPage, pageCount, deptId);
        return RestResult.success().data(map);

    }

    @Operation(summary = "删除文件", description = "可以删除文件或者目录", tags = {"departmentfile"})
    @RequestMapping(value = "/deletefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteFile(@RequestBody DeleteDeptFileDTO deleteDeptFileDTO, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        departmentfileService.deleteDeptFile(deleteDeptFileDTO.getDeptFileId(), sessionUser.getDeptId());

        return RestResult.success();

    }

    @Operation(summary = "批量删除文件", description = "批量删除文件", tags = {"departmentfile"})
    @RequestMapping(value = "/batchdeletefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> deleteImageByIds(@RequestBody BatchDeleteFileDTO batchDeleteFileDto,
                                               @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        List<DepartmentFile> departmentFiles = JSON.parseArray(batchDeleteFileDto.getFiles(), DepartmentFile.class);
        for (DepartmentFile departmentFile : departmentFiles) {
            departmentfileService.deleteDeptFile(departmentFile.getDeptFileId(), sessionUser.getDeptId());
        }

        return RestResult.success().message("批量删除文件成功");
    }

    @Operation(summary = "获取文件树", description = "文件移动的时候需要用到该接口，用来展示目录树", tags = {"departmentfile"})
    @RequestMapping(value = "/getfiletree", method = RequestMethod.GET)
    @ResponseBody
    public RestResult<TreeNodeVO> getFileTree(@RequestHeader("token") String token) {
        RestResult<TreeNodeVO> result = new RestResult<TreeNodeVO>();
        DepartmentFile departmentFile = new DepartmentFile();
        User sessionUser = userService.getUserByToken(token);
        departmentFile.setDeptId(sessionUser.getDeptId());

        List<DepartmentFile> filePathList = departmentfileService.selectFilePathTreeByDeptId(sessionUser.getDeptId());
        TreeNodeVO resultTreeNode = new TreeNodeVO();
        resultTreeNode.setLabel("根目录");

        for (int i = 0; i < filePathList.size(); i++) {
            String filePath = filePathList.get(i).getFilePath() + filePathList.get(i).getFileName() + "/";

            Queue<String> queue = new LinkedList<>();

            String[] strArr = filePath.split("/");
            for (int j = 0; j < strArr.length; j++) {
                if (!"".equals(strArr[j]) && strArr[j] != null) {
                    queue.add(strArr[j]);
                }

            }
            if (queue.size() == 0) {
                continue;
            }
            resultTreeNode = insertTreeNode(resultTreeNode, "/", queue);


        }
        result.setSuccess(true);
        result.setData(resultTreeNode);
        return result;

    }

    @Operation(summary = "文件移动", description = "可以移动文件或者目录", tags = {"departmentfile"})
    @RequestMapping(value = "/movefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> moveFile(@RequestBody MoveFileDTO moveFileDto, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        String oldfilePath = moveFileDto.getOldFilePath();
        String newfilePath = moveFileDto.getFilePath();
        String fileName = moveFileDto.getFileName();
        String extendName = moveFileDto.getExtendName();

        departmentfileService.updateFilepathByFilepath(oldfilePath, newfilePath, fileName, extendName, sessionUser.getDeptId());
        return RestResult.success();

    }

    @Operation(summary = "批量移动文件", description = "可以同时选择移动多个文件或者目录", tags = {"departmentfile"})
    @RequestMapping(value = "/batchmovefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> batchMoveFile(@RequestBody BatchMoveFileDTO batchMoveFileDto,
                                            @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        String files = batchMoveFileDto.getFiles();
        String newfilePath = batchMoveFileDto.getFilePath();
        List<DepartmentFile> departmentFiles = JSON.parseArray(files, DepartmentFile.class);

        for (DepartmentFile departmentFile : departmentFiles) {
            departmentfileService.updateFilepathByFilepath(departmentFile.getFilePath(), newfilePath, departmentFile.getFileName(),
                    departmentFile.getExtendName(), sessionUser.getDeptId());
        }

        return RestResult.success().data("批量移动文件成功");

    }

    @Operation(summary = "文件重命名", description = "文件重命名", tags = {"departmentfile"})
    @RequestMapping(value = "/renamefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult<String> renameFile(@RequestBody RenameDeptFileDTO renameDeptFileDto, @RequestHeader("token") String token) {
        User sessionUser = userService.getUserByToken(token);
        DepartmentFile departmentFile = departmentfileService.getById(renameDeptFileDto.getDeptFileId());

        List<DepartmentFile> departmentFiles = departmentfileService.selectDeptFileByNameAndPath(renameDeptFileDto.getFileName(), departmentFile.getFilePath(), sessionUser.getDeptId());
        if (departmentFiles != null && !departmentFiles.isEmpty()) {
            return RestResult.fail().message("同名文件已存在");

        }
        if (1 == departmentFile.getIsDir()) {
            LambdaUpdateWrapper<DepartmentFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(DepartmentFile::getFileName, renameDeptFileDto.getFileName())
                    .set(DepartmentFile::getUploadTime, DateUtil.getCurrentTime())
                    .eq(DepartmentFile::getDeptFileId, renameDeptFileDto.getDeptFileId());
            departmentfileService.update(lambdaUpdateWrapper);
            departmentfileService.replaceDeptFilePath(departmentFile.getFilePath() + renameDeptFileDto.getFileName() + "/",
                    departmentFile.getFilePath() + departmentFile.getFileName() + "/", sessionUser.getDeptId());
        } else {
            File file = fileService.getById(departmentFile.getFileId());

            LambdaUpdateWrapper<DepartmentFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.set(DepartmentFile::getFileName, renameDeptFileDto.getFileName())
                    .set(DepartmentFile::getUploadTime, DateUtil.getCurrentTime())
                    .eq(DepartmentFile::getDeptFileId, renameDeptFileDto.getDeptFileId());
            departmentfileService.update(lambdaUpdateWrapper);


        }

        return RestResult.success();
    }

    @Operation(summary = "文件搜索", description = "模糊搜索文件", tags = {"departmentfile"})
    @GetMapping(value = "/searchfile")
    @ResponseBody
    public RestResult<List<Map<String, Object>>> searchFile(String fileName, int fileType, Long currentPage, Long pageCount, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");
        }
        long deptId = sessionUser.getDeptId();

        Map<String, Object> map = departmentfileService.searchDeptFile(fileName, fileType, deptId, currentPage, pageCount);
        return RestResult.success().data(map);

    }


    @Operation(summary = "获取删除文件列表", description = "用来做前台删除文件列表展示", tags = {"departmentfile"})
    @GetMapping(value = "/getdeletefilelist")
    @ResponseBody
    public RestResult<DepartmentfileListVO> getDeletefileList(DepartmentFileListDTO departmentFileListDTO,
                                                        @RequestHeader("token") String token) {


        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");

        }

        List<DepartmentfileListVO> fileList = departmentfileService.getDeleteFileByFilePath(departmentFileListDTO.getFilePath(),
                sessionUser.getDeptId(), departmentFileListDTO.getCurrentPage(), departmentFileListDTO.getPageCount());

        LambdaQueryWrapper<DepartmentFile> deptFileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        deptFileLambdaQueryWrapper.eq(DepartmentFile::getDeptId, sessionUser.getDeptId())
                .eq(DepartmentFile::getFilePath, departmentFileListDTO.getFilePath()).eq(DepartmentFile::getDeleteFlag, 0);
        int total = departmentfileService.count(deptFileLambdaQueryWrapper);

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", fileList);

        return RestResult.success().data(map);

    }

    @Operation(summary = "还原文件", description = "可以还原已经删除的文件或者目录", tags = {"departmentfile"})
    @RequestMapping(value = "/restorefile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult restoreFile(@RequestBody DeleteDeptFileDTO deleteDeptFileDTO, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);

        departmentfileService.restoreDeptFile(deleteDeptFileDTO.getDeptFileId(), sessionUser.getDeptId());

        return RestResult.success();

    }

    @Operation(summary = "各文件下载量", description = "查询每个文件的下载量", tags = {"departmentfile"})
    @GetMapping(value = "/getdeptfiledownloads")
    @ResponseBody
    public List<DeptFileDownloadsVO> getDeptFileDownloads(){
        return departmentfileService.getDeptFileDownloads();
    }

    @Operation(summary = "删除回收站部门文件", description = "删除保存在回收站的部门文件", tags = {"departmentfile"})
    @RequestMapping(value = "/deleterecoverydeptfile", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteDeptRecoveryFile(@RequestBody DeleteDeptRecoveryDTO deleteDeptRecoveryDTO, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");

        }
        departmentfileService.deleteDeptRecoveryFile(deleteDeptRecoveryDTO.getDeptFileId());

        return RestResult.success();

    }

    @Operation(summary = "批量删除回收站部门文件", description = "批量删除保存在回收站的部门文件", tags = {"departmentfile"})
    @RequestMapping(value = "/deleterecoverydeptfilelist", method = RequestMethod.POST)
    @ResponseBody
    public RestResult deleteDeptRecoveryFileList(@RequestBody List<DeleteDeptRecoveryDTO> deleteDeptRecoveryDTO, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");

        }
        departmentfileService.deleteDeptRecoveryFileList(deleteDeptRecoveryDTO);

        return RestResult.success();

    }

    @Operation(summary = "添加评论", description = "添加评论", tags = {"departmentfile"})
    @RequestMapping(value = "/addreview", method = RequestMethod.POST)
    @ResponseBody
    public RestResult addReview(@RequestBody AddReviewDTO addReviewDTO, @RequestHeader("token") String token) {

        User sessionUser = userService.getUserByToken(token);
        if (sessionUser == null) {
            return RestResult.fail().message("token验证失败");

        }
        addReviewDTO.setUserId(sessionUser.getUserId());
        departmentfileService.addReview(addReviewDTO);

        return RestResult.success();

    }

    @Operation(summary = "获取评论列表", description = "获取文件评论列表", tags = {"departmentfile"})
    @GetMapping(value = "/getreviews")
    @ResponseBody
    public RestResult<List<ReviewVO>> getReviews(Long fileId){
        return RestResult.success().data(departmentfileService.searchReview(fileId));
    }
}
