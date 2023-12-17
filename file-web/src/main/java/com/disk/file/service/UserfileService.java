package com.disk.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.disk.file.dto.DeleteRecoveryDTO;
import com.disk.file.model.UserFile;
import com.disk.file.vo.UserfileListVO;

import java.util.List;
import java.util.Map;

public interface UserfileService extends IService<UserFile> {
    List<UserfileListVO> getUserFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount);
    Map<String, Object> getUserFileByType(int fileType, Long currentPage, Long pageCount, Long userId);
    void deleteUserFile(Long userFileId, Long sessionUserId);
    List<UserFile> selectFileTreeListLikeFilePath(String filePath, long userId);
    List<UserFile> selectFilePathTreeByUserId(Long userId);
    void updateFilepathByFilepath(String oldfilePath, String newfilePath, String fileName, String extendName, Long userId);
    List<UserFile> selectUserFileByNameAndPath(String fileName, String filePath, Long userId);
    void replaceUserFilePath(String filePath, String oldFilePath, Long userId);
    Map<String, Object> searchUserFile(String fileName, int fileType, Long userId, Long currentPage, Long pageCount);
    List<UserfileListVO> getDeleteFileByFilePath(String filePath, Long userId, Long currentPage, Long pageCount);
    void restoreUserFile(Long userFileId, Long sessionUserId);
    void deleteRecoveryFile(Long userFileId);
    void deleteRecoveryFileList(List<DeleteRecoveryDTO> deleteRecoveryDTOList);
}
