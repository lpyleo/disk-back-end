package com.disk.file.operation;

import com.disk.file.operation.delete.Deleter;
import com.disk.file.operation.download.Downloader;
import com.disk.file.operation.upload.Uploader;

public interface FileOperationFactory {
    Uploader getUploader();
    Downloader getDownloader();
    Deleter getDeleter();
}