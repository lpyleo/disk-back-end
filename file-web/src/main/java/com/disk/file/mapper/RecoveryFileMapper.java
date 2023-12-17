package com.disk.file.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.disk.file.model.RecoveryFile;
import com.disk.file.vo.RecoveryFileListVO;

public interface RecoveryFileMapper extends BaseMapper<RecoveryFile> {
    List<RecoveryFileListVO> selectRecoveryFileList();
}