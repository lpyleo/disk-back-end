package com.disk.file.operation.delete;

import com.disk.file.operation.delete.domain.DeleteFile;

public abstract class Deleter {
    public abstract void delete(DeleteFile deleteFile);
}