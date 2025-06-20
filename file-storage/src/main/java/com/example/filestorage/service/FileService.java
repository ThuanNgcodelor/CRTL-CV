package com.example.filestorage.service;

import com.example.filestorage.model.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImageToFileSystem(MultipartFile file);
    byte[] downloadFileFromFileSystem(String id);
    void deleteFileFromFileSystem(String id);
    File findFileById(String id);
}
