package com.disk.file.service;

import com.disk.file.dto.DownloadDeptFileDTO;
import com.disk.file.dto.DownloadFileDTO;
import com.disk.file.dto.UploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface DeptFiletransferService {
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long deptId);
    void downloadFile(HttpServletResponse httpServletResponse, DownloadDeptFileDTO downloadDeptFileDTO);
    Long selectStorageSizeByDeptId(Long deptId);
}
