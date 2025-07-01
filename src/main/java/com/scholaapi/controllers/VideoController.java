package com.scholaapi.controllers;

import com.scholaapi.dto.VideoRequest;
import com.scholaapi.dto.VideoResponseDTO;
import com.scholaapi.model.Resource;
import com.scholaapi.model.Video;
import com.scholaapi.service.FileUploadService;
import com.scholaapi.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VideoController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private FileUploadService fileUploadService;

    // Upload video file
    @PostMapping("/videos/upload")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            if (!fileUploadService.isValidVideoFile(file)) {
                return ResponseEntity.badRequest().body("Invalid video file format");
            }

            String filename = fileUploadService.uploadVideo(file);
            return ResponseEntity.ok("Video uploaded successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload video: " + e.getMessage());
        }
    }

    // Combined upload video with metadata
    @PostMapping("/videos/upload-with-metadata")
    public ResponseEntity<VideoResponseDTO> uploadVideoWithMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam("moduleUuid") UUID moduleUuid,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "duration", required = false) String duration) {
        try {
            if (!fileUploadService.isValidVideoFile(file)) {
                return ResponseEntity.badRequest().body(null);
            }

            // Upload the file first
            String filename = fileUploadService.uploadVideo(file);
            
            // Create video request
            VideoRequest request = new VideoRequest();
            request.setModuleUuid(moduleUuid);
            request.setTitle(title);
            request.setDescription(description != null ? description : "");
            request.setDuration(duration != null ? duration : "");
            
            // Create the resource
            Resource resource = resourceService.createVideoResource(request);
            
            // Update the video entity with the filename
            Video video = resource.getVideo();
            video.setFilename(filename);
            resourceService.updateVideoFilename(video.getUuid(), filename);
            
            // Return DTO
            return ResponseEntity.ok(resourceService.toVideoResponseDTO(video));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Create video resource
    @PostMapping("/videos/create")
    public ResponseEntity<?> createVideoResource(@RequestBody VideoRequest request) {
        return ResponseEntity.ok(resourceService.createVideoResource(request));
    }

    // Get all videos in a module
    @GetMapping("/modules/{moduleUuid}/videos")
    public ResponseEntity<?> getVideosByModule(@PathVariable UUID moduleUuid) {
        return ResponseEntity.ok(resourceService.getVideosByModule(moduleUuid));
    }

    // Get specific video resource
    @GetMapping("/videos/{videoUuid}")
    public ResponseEntity<VideoResponseDTO> getVideoResource(@PathVariable UUID videoUuid) {
        Resource resource = resourceService.getVideoResource(videoUuid);
        Video video = resource.getVideo();
        return ResponseEntity.ok(resourceService.toVideoResponseDTO(video));
    }

    // Download video file
    @GetMapping("/videos/{videoUuid}/download")
    public ResponseEntity<?> downloadVideo(@PathVariable UUID videoUuid) {
        try {
            Resource resource = resourceService.getVideoResource(videoUuid);
            byte[] fileData = fileUploadService.getFile(resource.getVideo().getFilename(), "video");
            
            ByteArrayResource byteArrayResource = new ByteArrayResource(fileData);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getVideo().getTitle() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArrayResource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Stream video file
    @GetMapping("/videos/{videoUuid}/stream")
    public ResponseEntity<?> streamVideo(@PathVariable UUID videoUuid) {
        try {
            Resource resource = resourceService.getVideoResource(videoUuid);
            byte[] fileData = fileUploadService.getFile(resource.getVideo().getFilename(), "video");
            
            ByteArrayResource byteArrayResource = new ByteArrayResource(fileData);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("video/mp4"))
                    .body(byteArrayResource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update video resource
    @PutMapping("/videos/{videoUuid}")
    public ResponseEntity<?> updateVideoResource(@PathVariable UUID videoUuid, @RequestBody VideoRequest request) {
        return ResponseEntity.ok(resourceService.updateVideoResource(videoUuid, request));
    }

    // Delete video resource
    @DeleteMapping("/videos/{videoUuid}")
    public ResponseEntity<?> deleteVideoResource(@PathVariable UUID videoUuid) {
        resourceService.deleteVideoResource(videoUuid);
        return ResponseEntity.ok("Video resource deleted successfully.");
    }
} 