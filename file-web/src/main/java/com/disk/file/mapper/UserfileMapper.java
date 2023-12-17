package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.UserFile;
import com.disk.file.vo.UserfileListVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserfileMapper extends BaseMapper<UserFile> {
    List<UserfileListVO> userfileList(UserFile userfile, Long beginCount, Long pageCount);

    Long userfileCount(UserFile userfile, Long beginCount, Long pageCount);

    List<UserfileListVO> selectFileByExtendName(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    Long selectCountByExtendName(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    List<UserfileListVO> selectFileNotInExtendNames(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    Long selectCountNotInExtendNames(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long userId);

    void updateFilepathByFilepath(String oldfilePath, String newfilePath, Long userId);

    void replaceFilePath(@Param("filePath") String filePath, @Param("oldFilePath") String oldFilePath, @Param("userId") Long userId);

    Long selectStorageSizeByUserId(@Param("userId") Long userId);

    List<UserfileListVO> deletefileList(UserFile userfile, Long beginCount, Long pageCount);

    Long deletefileCount(UserFile userfile, Long beginCount, Long pageCount);

    void restoreFile(UserFile userfile);

    void deleteRecoveryFile(Long userFileId);

}
