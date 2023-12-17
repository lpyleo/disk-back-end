package com.disk.file.operation;

import javax.annotation.Resource;

import com.disk.file.operation.delete.Deleter;
import com.disk.file.operation.delete.product.LocalStorageDeleter;
import com.disk.file.operation.download.Downloader;
import com.disk.file.operation.download.product.LocalStorageDownloader;
import com.disk.file.operation.upload.Uploader;
import com.disk.file.operation.upload.product.LocalStorageUploader;

import org.springframework.stereotype.Component;

@Component
public class LocalStorageOperationFactory implements FileOperationFactory{

    @Resource
    LocalStorageUploader localStorageUploader;
    @Resource
    LocalStorageDownloader localStorageDownloader;
    @Resource
    LocalStorageDeleter localStorageDeleter;
    @Override
    public Uploader getUploader() {
        return localStorageUploader;
    }

    @Override
    public Downloader getDownloader() {
        return localStorageDownloader;
    }

    @Override
    public Deleter getDeleter() {
        return localStorageDeleter;
    }


}