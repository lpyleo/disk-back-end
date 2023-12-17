package com.disk.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.RecoveryDeptFile;
import com.disk.file.vo.RecoveryDeptFileListVO;

import java.util.List;

public interface RecoveryDeptFileMapper extends BaseMapper<RecoveryDeptFile> {
    List<RecoveryDeptFileListVO> selectRecoveryDeptFileList();
}
