package com.example.filestorage.controller;

import com.example.filestorage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/file-storage")
@RequiredArgsConstructor
public class StorageController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart("image") MultipartFile file) {
        return ResponseEntity.ok()
                .body(fileService.uploadImageToFileSystem(file));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String id) {
        return ResponseEntity.ok()
                .body(fileService.downloadFileFromFileSystem(id));
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> deleteImageFromFileSystem(@PathVariable String id) {
        fileService.deleteFileFromFileSystem(id);
        return ResponseEntity.ok().build();
    }
}
