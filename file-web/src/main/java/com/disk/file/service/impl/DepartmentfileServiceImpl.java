package com.disk.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.disk.file.constant.DeptFileConstant;
import com.disk.file.constant.FileConstant;
import com.disk.file.dto.AddReviewDTO;
import com.disk.file.dto.DeleteDeptRecoveryDTO;
import com.disk.file.dto.DeleteRecoveryDTO;
import com.disk.file.mapper.*;
import com.disk.file.model.*;
import com.disk.file.service.DepartmentfileService;
import com.disk.file.util.DateUtil;
import com.disk.file.vo.DepartmentfileListVO;
import com.disk.file.vo.DeptFileDownloadsVO;
import com.disk.file.vo.ReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class DepartmentfileServiceImpl extends ServiceImpl<DepartmentFileMapper, DepartmentFile> implements DepartmentfileService {

    public static Executor executor = Executors.newFixedThreadPool(20);
    @Resource
    DepartmentFileMapper departmentFileMapper;
    @Resource
    FileMapper fileMapper;
    @Resource
    RecoveryDeptFileMapper recoveryDeptFileMapper;
    @Resource
    ReviewMapper reviewMapper;

    @Override
    public List<DepartmentfileListVO> getDeptFileByFilePath(String filePath, Long deptId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        DepartmentFile departmentFile = new DepartmentFile();
        departmentFile.setDeptId(deptId);
        departmentFile.setFilePath(filePath);
        List<DepartmentfileListVO> fileList = departmentFileMapper.departmentfileList(departmentFile, beginCount, pageCount);
        return fileList;
    }

    @Override
    public Map<String, Object> getDeptFileByType(int fileType, Long currentPage, Long pageCount, Long deptId) {
        Long beginCount = (currentPage - 1) * pageCount;
        List<DepartmentfileListVO> fileList;
        Long total;
        if (fileType == DeptFileConstant.OTHER_TYPE) {

            List<String> arrList = new ArrayList<>();
            arrList.addAll(Arrays.asList(DeptFileConstant.DOC_FILE));
            arrList.addAll(Arrays.asList(DeptFileConstant.IMG_FILE));
            arrList.addAll(Arrays.asList(DeptFileConstant.VIDEO_FILE));
            arrList.addAll(Arrays.asList(DeptFileConstant.MUSIC_FILE));


            fileList = departmentFileMapper.selectFileNotInExtendNames(null, arrList, beginCount, pageCount, deptId);
            total = departmentFileMapper.selectCountNotInExtendNames(null, arrList, beginCount, pageCount, deptId);
        } else {
            List<String> fileExtends = null;
            if (fileType == DeptFileConstant.IMAGE_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.IMG_FILE);
            } else if (fileType == DeptFileConstant.DOC_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.DOC_FILE);
            } else if (fileType == DeptFileConstant.VIDEO_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.VIDEO_FILE);
            } else if (fileType == DeptFileConstant.MUSIC_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.MUSIC_FILE);
            }
            fileList = departmentFileMapper.selectFileByExtendName(null, fileExtends, beginCount, pageCount, deptId);
            total = departmentFileMapper.selectCountByExtendName(null, fileExtends, beginCount, pageCount, deptId);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", fileList);
        map.put("total", total);
        return map;
    }

    @Override
    public void deleteDeptFile(Long deptFileId, Long deptId) {

        DepartmentFile departmentFile = departmentFileMapper.selectById(deptFileId);
        String uuid = UUID.randomUUID().toString();
        if (departmentFile.getIsDir() == 1) {
            LambdaUpdateWrapper<DepartmentFile> deptFileLambdaUpdateWrapper = new LambdaUpdateWrapper<DepartmentFile>();
            deptFileLambdaUpdateWrapper.set(DepartmentFile::getDeleteFlag, 1)
                    .set(DepartmentFile::getDeleteBatchNum, uuid)
                    .set(DepartmentFile::getDeleteTime, DateUtil.getCurrentTime())
                    .eq(DepartmentFile::getDeptFileId, deptFileId);
            departmentFileMapper.update(null, deptFileLambdaUpdateWrapper);

            String filePath = departmentFile.getFilePath() + departmentFile.getFileName() + "/";
            updateFileDeleteStateByFilePath(filePath, departmentFile.getDeleteBatchNum(), deptId);

        } else {

            DepartmentFile deptFileTemp = departmentFileMapper.selectById(deptFileId);

            File file = fileMapper.selectById(deptFileTemp.getFileId());
            LambdaUpdateWrapper<DepartmentFile> deptFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            deptFileLambdaUpdateWrapper.set(DepartmentFile::getDeleteFlag, 1)
                    .set(DepartmentFile::getDeleteTime, DateUtil.getCurrentTime())
                    .set(DepartmentFile::getDeleteBatchNum, uuid)
                    .eq(DepartmentFile::getDeptFileId, deptFileTemp.getDeptFileId());
            departmentFileMapper.update(null, deptFileLambdaUpdateWrapper);

        }

        RecoveryDeptFile recoveryDeptFile = new RecoveryDeptFile();
        recoveryDeptFile.setDeptFileId(deptFileId);
        recoveryDeptFile.setDeleteTime(DateUtil.getCurrentTime());
        recoveryDeptFile.setDeleteBatchNum(uuid);
        recoveryDeptFileMapper.insert(recoveryDeptFile);


    }


    @Override
    public List<DepartmentFile> selectFileTreeListLikeFilePath(String filePath, long deptId) {
        filePath = filePath.replace("\\", "\\\\\\\\");
        filePath = filePath.replace("'", "\\'");
        filePath = filePath.replace("%", "\\%");
        filePath = filePath.replace("_", "\\_");


        LambdaQueryWrapper<DepartmentFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        log.info("查询文件路径：" + filePath);

        lambdaQueryWrapper.eq(DepartmentFile::getDeptId, deptId).likeRight(DepartmentFile::getFilePath, filePath);
        return departmentFileMapper.selectList(lambdaQueryWrapper);
    }

    private void updateFileDeleteStateByFilePath(String filePath, String deleteBatchNum, Long deptId) {
        new Thread(() -> {
            List<DepartmentFile> fileList = selectFileTreeListLikeFilePath(filePath, deptId);
            for (int i = 0; i < fileList.size(); i++) {
                DepartmentFile deptFileTemp = fileList.get(i);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //标记删除标志
                        LambdaUpdateWrapper<DepartmentFile> deptFileLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                        deptFileLambdaUpdateWrapper1.set(DepartmentFile::getDeleteFlag, 1)
                                .set(DepartmentFile::getDeleteTime, DateUtil.getCurrentTime())
                                .set(DepartmentFile::getDeleteBatchNum, deleteBatchNum)
                                .eq(DepartmentFile::getDeptFileId, deptFileTemp.getDeptFileId())
                                .eq(DepartmentFile::getDeleteFlag, 0);
                        departmentFileMapper.update(null, deptFileLambdaUpdateWrapper1);
                    }
                });

            }
        }).start();
    }

    private void updateFileRestoreStateByFilePath(String filePath, String deleteBatchNum, Long deptId) {
        new Thread(() -> {
            List<DepartmentFile> fileList = selectFileTreeListLikeFilePath(filePath, deptId);
            for (int i = 0; i < fileList.size(); i++) {
                DepartmentFile deptFileTemp = fileList.get(i);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //标记删除标志
                        LambdaUpdateWrapper<DepartmentFile> deptFileLambdaUpdateWrapper1 = new LambdaUpdateWrapper<>();
                        deptFileLambdaUpdateWrapper1.set(DepartmentFile::getDeleteFlag, 0)
                                .set(DepartmentFile::getDeleteTime, DateUtil.getCurrentTime())
                                .set(DepartmentFile::getDeleteBatchNum, deleteBatchNum)
                                .eq(DepartmentFile::getDeptFileId, deptFileTemp.getDeptFileId())
                                .eq(DepartmentFile::getDeleteFlag, 1);
                        departmentFileMapper.update(null, deptFileLambdaUpdateWrapper1);
                    }
                });

            }
        }).start();
    }

    @Override
    public void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long deptId) {
        if ("null".equals(extendName)) {
            extendName = null;
        }

        LambdaUpdateWrapper<DepartmentFile> lambdaUpdateWrapper = new LambdaUpdateWrapper<DepartmentFile>();
        lambdaUpdateWrapper.set(DepartmentFile::getFilePath, newfilePath)
                .eq(DepartmentFile::getFilePath, oldfilePath)
                .eq(DepartmentFile::getFileName, fileName)
                .eq(DepartmentFile::getDeptId, deptId);
        if (StringUtils.isNotEmpty(extendName)) {
            lambdaUpdateWrapper.eq(DepartmentFile::getExtendName, extendName);
        } else {
            lambdaUpdateWrapper.isNull(DepartmentFile::getExtendName);
        }
        departmentFileMapper.update(null, lambdaUpdateWrapper);
        //移动子目录
        oldfilePath = oldfilePath + fileName + "/";
        newfilePath = newfilePath + fileName + "/";

        oldfilePath = oldfilePath.replace("\\", "\\\\\\\\");
        oldfilePath = oldfilePath.replace("'", "\\'");
        oldfilePath = oldfilePath.replace("%", "\\%");
        oldfilePath = oldfilePath.replace("_", "\\_");

        if (extendName == null) { //为null说明是目录，则需要移动子目录
            departmentFileMapper.updateFilepathByFilepath(oldfilePath, newfilePath, deptId);
        }
    }

    @Override
    public List<DepartmentFile> selectFilePathTreeByDeptId(Long deptId) {
        LambdaQueryWrapper<DepartmentFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DepartmentFile::getDeptId, deptId)
                .eq(DepartmentFile::getIsDir, 1)
                .eq(DepartmentFile::getDeleteFlag, 0);
        return departmentFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public List<DepartmentFile> selectDeptFileByNameAndPath(String fileName, String filePath, Long deptId) {
        LambdaQueryWrapper<DepartmentFile> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DepartmentFile::getFileName, fileName)
                .eq(DepartmentFile::getFilePath, filePath)
                .eq(DepartmentFile::getDeptId, deptId)
                .eq(DepartmentFile::getDeleteFlag, "0");
        return departmentFileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void replaceDeptFilePath(String filePath, String oldFilePath, Long deptId) {
        departmentFileMapper.replaceFilePath(filePath, oldFilePath, deptId);
    }

    @Override
    public Map<String, Object> searchDeptFile(String fileName, int fileType, Long deptId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        List<DepartmentfileListVO> fileList;
        Long total;


        if(fileType == 7){
            DepartmentFile departmentFile = new DepartmentFile();
            departmentFile.setDeptId(deptId);
            departmentFile.setFileName(fileName);
            fileList = departmentFileMapper.departmentfileList(departmentFile, beginCount, pageCount);
            total = departmentFileMapper.departmentfileCount(departmentFile, beginCount, pageCount);
        } else if (fileType == DeptFileConstant.OTHER_TYPE) {

            List<String> arrList = new ArrayList<>();
            arrList.addAll(Arrays.asList(DeptFileConstant.DOC_FILE));
            arrList.addAll(Arrays.asList(DeptFileConstant.IMG_FILE));
            arrList.addAll(Arrays.asList(DeptFileConstant.VIDEO_FILE));
            arrList.addAll(Arrays.asList(DeptFileConstant.MUSIC_FILE));

            fileList = departmentFileMapper.selectFileNotInExtendNames(fileName, arrList, beginCount, pageCount, deptId);
            total = departmentFileMapper.selectCountNotInExtendNames(fileName, arrList, beginCount, pageCount, deptId);
        } else if (fileType == 13) {
            DepartmentFile departmentFile = new DepartmentFile();
            departmentFile.setDeptId(deptId);
            departmentFile.setFileName(fileName);
            fileList = departmentFileMapper.deletefileList(departmentFile, beginCount, pageCount);
            total = departmentFileMapper.deletefileCount(departmentFile, beginCount, pageCount);
        } else {
            List<String> fileExtends = null;
            if (fileType == DeptFileConstant.IMAGE_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.IMG_FILE);
            } else if (fileType == DeptFileConstant.DOC_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.DOC_FILE);
            } else if (fileType == DeptFileConstant.VIDEO_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.VIDEO_FILE);
            } else if (fileType == DeptFileConstant.MUSIC_TYPE) {
                fileExtends = Arrays.asList(DeptFileConstant.MUSIC_FILE);
            }
            fileList = departmentFileMapper.selectFileByExtendName(fileName, fileExtends, beginCount, pageCount, deptId);
            total = departmentFileMapper.selectCountByExtendName(fileName, fileExtends, beginCount, pageCount, deptId);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("list", fileList);
        map.put("total", total);
        return map;
    }

    @Override
    public List<DepartmentfileListVO> getDeleteFileByFilePath(String filePath, Long deptId, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        DepartmentFile departmentFile = new DepartmentFile();
        departmentFile.setDeptId(deptId);
        departmentFile.setFilePath(null);
        List<DepartmentfileListVO> fileList = departmentFileMapper.deletefileList(departmentFile, beginCount, pageCount);
        return fileList;
    }

    @Override
    public void restoreDeptFile(Long deptFileId, Long deptId) {

        DepartmentFile departmentFile = departmentFileMapper.selectById(deptFileId);
        String uuid = UUID.randomUUID().toString();
        if (departmentFile.getIsDir() == 1) {
            LambdaUpdateWrapper<DepartmentFile> deptFileLambdaUpdateWrapper = new LambdaUpdateWrapper<DepartmentFile>();
            deptFileLambdaUpdateWrapper.set(DepartmentFile::getDeleteFlag, 0)
                    .set(DepartmentFile::getDeleteBatchNum, uuid)
                    .set(DepartmentFile::getDeleteTime, DateUtil.getCurrentTime())
                    .eq(DepartmentFile::getDeptFileId, deptFileId);
            departmentFileMapper.update(null, deptFileLambdaUpdateWrapper);

            String filePath = departmentFile.getFilePath() + departmentFile.getFileName() + "/";
            updateFileRestoreStateByFilePath(filePath, departmentFile.getDeleteBatchNum(), deptId);

        } else {

            DepartmentFile deptFileTemp = departmentFileMapper.selectById(deptFileId);
            File file = fileMapper.selectById(deptFileTemp.getFileId());

            LambdaUpdateWrapper<DepartmentFile> deptFileLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            deptFileLambdaUpdateWrapper.set(DepartmentFile::getDeleteFlag, 0)
                    .set(DepartmentFile::getDeleteTime, DateUtil.getCurrentTime())
                    .set(DepartmentFile::getDeleteBatchNum, uuid)
                    .eq(DepartmentFile::getDeptFileId, deptFileTemp.getDeptFileId());
            departmentFileMapper.update(null, deptFileLambdaUpdateWrapper);

        }
    }

    @Override
    public List<DeptFileDownloadsVO> getDeptFileDownloads(){
        return departmentFileMapper.getDeptFileDownloads();
    }

    @Override
    public void deleteDeptRecoveryFile(Long deptFileId){
        departmentFileMapper.deleteDeptRecoveryFile(deptFileId);
    }

    @Override
    public void deleteDeptRecoveryFileList(List<DeleteDeptRecoveryDTO> deletedeptRecoveryDTOList){
        for(int i = 0; i < deletedeptRecoveryDTOList.size(); i++){
            departmentFileMapper.deleteDeptRecoveryFile(deletedeptRecoveryDTOList.get(i).getDeptFileId());
        }
    }

    @Override
    public void addReview(AddReviewDTO addReviewDTO){
        Review review = new Review();
        review.setFileId(addReviewDTO.getFileId());
        review.setUserId(addReviewDTO.getUserId());
        review.setContent(addReviewDTO.getContent());
        review.setReviewTime(addReviewDTO.getReviewTime());
        reviewMapper.addReview(review);
    }

    @Override
    public List<ReviewVO> searchReview(Long fileId){
        return reviewMapper.selectAllReviewByFileId(fileId);
    }
}
