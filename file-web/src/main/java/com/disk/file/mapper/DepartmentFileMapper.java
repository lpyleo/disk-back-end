package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.DepartmentFile;
import com.disk.file.vo.DepartmentfileListVO;
import com.disk.file.vo.DeptFileDownloadsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentFileMapper extends BaseMapper<DepartmentFile> {
    List<DepartmentfileListVO> departmentfileList(DepartmentFile departmentfile, Long beginCount, Long pageCount);

    Long departmentfileCount(DepartmentFile departmentfile, Long beginCount, Long pageCount);

    List<DepartmentfileListVO> selectFileByExtendName(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long deptId);

    Long selectCountByExtendName(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long deptId);

    List<DepartmentfileListVO> selectFileNotInExtendNames(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long deptId);

    Long selectCountNotInExtendNames(String fileName, List<String> fileNameList, Long beginCount, Long pageCount, long deptId);

    void updateFilepathByFilepath(String oldfilePath, String newfilePath, Long deptId);

    void replaceFilePath(@Param("filePath") String filePath, @Param("oldFilePath") String oldFilePath, @Param("deptId") Long deptId);

    Long selectStorageSizeByDepartmentId(@Param("deptId") Long deptId);

    List<DepartmentfileListVO> deletefileList(DepartmentFile departmentfile, Long beginCount, Long pageCount);

    Long deletefileCount(DepartmentFile departmentfile, Long beginCount, Long pageCount);

    void restoreFile(DepartmentFile departmentfile);

    void updateDownloads(@Param("deptFileId") Long deptFileId);

    List<DeptFileDownloadsVO> getDeptFileDownloads();

    void deleteDeptRecoveryFile(Long deptFileId);
}
