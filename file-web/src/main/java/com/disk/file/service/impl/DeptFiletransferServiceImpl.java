package com.disk.file.service.impl;

import com.disk.file.dto.DownloadDeptFileDTO;
import com.disk.file.dto.UploadFileDTO;
import com.disk.file.mapper.DepartmentFileMapper;
import com.disk.file.mapper.FileMapper;
import com.disk.file.model.DepartmentFile;
import com.disk.file.model.File;
import com.disk.file.operation.FileOperationFactory;
import com.disk.file.operation.download.Downloader;
import com.disk.file.operation.download.domain.DownloadFile;
import com.disk.file.operation.upload.Uploader;
import com.disk.file.operation.upload.domain.UploadFile;
import com.disk.file.service.DeptFiletransferService;
import com.disk.file.service.FiletransferService;
import com.disk.file.util.DateUtil;
import com.disk.file.util.PropertiesUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class DeptFiletransferServiceImpl implements DeptFiletransferService {
    @Resource
    FileMapper fileMapper;
    @Resource
    DepartmentFileMapper departmentFileMapper;
    @Resource
    FileOperationFactory localStorageOperationFactory;

    @Override
    public void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long deptId) {

        Uploader uploader = null;
        UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(uploadFileDto.getChunkNumber());
        uploadFile.setChunkSize(uploadFileDto.getChunkSize());
        uploadFile.setTotalChunks(uploadFileDto.getTotalChunks());
        uploadFile.setIdentifier(uploadFileDto.getIdentifier());
        uploadFile.setTotalSize(uploadFileDto.getTotalSize());
        uploadFile.setCurrentChunkSize(uploadFileDto.getCurrentChunkSize());
        String storageType = PropertiesUtil.getProperty("file.storage-type");
        synchronized (FiletransferService.class) {
            if ("0".equals(storageType)) {
                uploader = localStorageOperationFactory.getUploader();
            }
        }

        List<UploadFile> uploadFileList = uploader.upload(request, uploadFile);
        for (int i = 0; i < uploadFileList.size(); i++){
            uploadFile = uploadFileList.get(i);
            File file = new File();

            file.setIdentifier(uploadFileDto.getIdentifier());
            file.setStorageType(Integer.parseInt(storageType));
            file.setTimeStampName(uploadFile.getTimeStampName());
            if (uploadFile.getSuccess() == 1){
                file.setFileUrl(uploadFile.getUrl());
                file.setFileSize(uploadFile.getFileSize());
                file.setFileName(uploadFile.getFileName());
                file.setPointCount(1);
                fileMapper.insert(file);
                DepartmentFile departmentFile = new DepartmentFile();
                departmentFile.setFileId(file.getFileId());
                departmentFile.setExtendName(uploadFile.getFileType());
                departmentFile.setFileName(uploadFile.getFileName());
                departmentFile.setFilePath(uploadFileDto.getFilePath());
                departmentFile.setDeleteFlag(0);
                departmentFile.setDeptId(deptId);
                departmentFile.setIsDir(0);
                departmentFile.setUploadTime(DateUtil.getCurrentTime());
                departmentFileMapper.insert(departmentFile);
            }

        }
    }

    @Override
    public void downloadFile(HttpServletResponse httpServletResponse, DownloadDeptFileDTO downloadDeptFileDTO) {
        DepartmentFile departmentFile = departmentFileMapper.selectById(downloadDeptFileDTO.getDeptFileId());

        String fileName = departmentFile.getFileName() + "." + departmentFile.getExtendName();
        try {
            fileName = new String(fileName.getBytes("utf-8"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
        httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名


        File file = fileMapper.selectById(departmentFile.getFileId());
        Downloader downloader = null;
        if (file.getStorageType() == 0) {
            downloader = localStorageOperationFactory.getDownloader();
        }
        DownloadFile uploadFile = new DownloadFile();
        uploadFile.setFileUrl(file.getFileUrl());
        uploadFile.setTimeStampName(file.getTimeStampName());
        downloader.download(httpServletResponse, uploadFile);
        departmentFileMapper.updateDownloads(downloadDeptFileDTO.getDeptFileId());
    }

    @Override
    public Long selectStorageSizeByDeptId(Long deptId) {
        return departmentFileMapper.selectStorageSizeByDepartmentId(deptId);
    }
}
