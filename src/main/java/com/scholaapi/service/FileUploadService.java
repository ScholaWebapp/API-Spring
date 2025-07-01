package com.scholaapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Value("${file.upload.videos:uploads/videos}")
    private String videosPath;

    @Value("${file.upload.documents:uploads/documents}")
    private String documentsPath;

    public String uploadVideo(MultipartFile file) throws IOException {
        return uploadFile(file, videosPath, "video");
    }

    public String uploadDocument(MultipartFile file) throws IOException {
        return uploadFile(file, documentsPath, "document");
    }

    private String uploadFile(MultipartFile file, String directory, String type) throws IOException {
        // Create directory if it doesn't exist
        Path uploadDir = Paths.get(directory);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        // Save file
        Path filePath = uploadDir.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    public byte[] getFile(String filename, String type) throws IOException {
        String directory = type.equals("video") ? videosPath : documentsPath;
        Path filePath = Paths.get(directory, filename);
        
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filename);
        }
        
        return Files.readAllBytes(filePath);
    }

    public void deleteFile(String filename, String type) throws IOException {
        String directory = type.equals("video") ? videosPath : documentsPath;
        Path filePath = Paths.get(directory, filename);
        
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    public boolean isValidVideoFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("video/");
    }

    public boolean isValidDocumentFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
            contentType.equals("application/pdf") ||
            contentType.equals("application/msword") ||
            contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
            contentType.equals("text/plain")
        );
    }
} 