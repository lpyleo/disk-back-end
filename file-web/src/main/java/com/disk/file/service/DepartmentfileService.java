package com.disk.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disk.file.dto.AddReviewDTO;
import com.disk.file.dto.DeleteDeptRecoveryDTO;
import com.disk.file.dto.DeleteRecoveryDTO;
import com.disk.file.model.DepartmentFile;
import com.disk.file.vo.DepartmentfileListVO;
import com.disk.file.vo.DeptFileDownloadsVO;
import com.disk.file.vo.ReviewVO;

import java.util.List;
import java.util.Map;

public interface DepartmentfileService extends IService<DepartmentFile> {
    List<DepartmentfileListVO> getDeptFileByFilePath(String filePath, Long deptId, Long currentPage, Long pageCount);
    Map<String, Object> getDeptFileByType(int fileType, Long currentPage, Long pageCount, Long deptId);
    void deleteDeptFile(Long deptFileId, Long deptId);
    List<DepartmentFile> selectFileTreeListLikeFilePath(String filePath, long deptId);
    List<DepartmentFile> selectFilePathTreeByDeptId(Long deptId);
    void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long deptId);
    List<DepartmentFile> selectDeptFileByNameAndPath(String fileName, String filePath, Long deptId);
    void replaceDeptFilePath(String filePath, String oldFilePath, Long deptId);
    Map<String, Object> searchDeptFile(String fileName, int fileType, Long deptId, Long currentPage, Long pageCount);
    List<DepartmentfileListVO> getDeleteFileByFilePath(String filePath, Long deptId, Long currentPage, Long pageCount);
    void restoreDeptFile(Long deptFileId, Long deptId);
    List<DeptFileDownloadsVO> getDeptFileDownloads();
    void deleteDeptRecoveryFile(Long deptFileId);
    void deleteDeptRecoveryFileList(List<DeleteDeptRecoveryDTO> deleteDeptRecoveryDTOList);
    void addReview(AddReviewDTO addReviewDTO);
    List<ReviewVO> searchReview(Long fileId);
}
