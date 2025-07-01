package com.scholaapi.controllers;

import com.scholaapi.dto.DocumentRequest;
import com.scholaapi.dto.DocumentResponseDTO;
import com.scholaapi.model.Resource;
import com.scholaapi.model.Document;
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
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private FileUploadService fileUploadService;

    // Upload document file
    @PostMapping("/documents/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            if (!fileUploadService.isValidDocumentFile(file)) {
                return ResponseEntity.badRequest().body("Invalid document file format");
            }

            String filename = fileUploadService.uploadDocument(file);
            return ResponseEntity.ok("Document uploaded successfully: " + filename);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to upload document: " + e.getMessage());
        }
    }

    // Combined upload document with metadata
    @PostMapping("/documents/upload-with-metadata")
    public ResponseEntity<DocumentResponseDTO> uploadDocumentWithMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam("moduleUuid") UUID moduleUuid,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("fileType") String fileType) {
        try {
            if (!fileUploadService.isValidDocumentFile(file)) {
                return ResponseEntity.badRequest().body(null);
            }

            // Upload the file first
            String filename = fileUploadService.uploadDocument(file);
            
            // Create document request
            DocumentRequest request = new DocumentRequest();
            request.setModuleUuid(moduleUuid);
            request.setTitle(title);
            request.setDescription(description != null ? description : "");
            request.setFileType(fileType);
            
            // Create the resource
            Resource resource = resourceService.createDocumentResource(request);
            
            // Update the document entity with the filename
            Document document = resource.getDocument();
            document.setFilename(filename);
            resourceService.updateDocumentFilename(document.getUuid(), filename);
            
            // Return DTO
            return ResponseEntity.ok(resourceService.toDocumentResponseDTO(document));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // Create document resource
    @PostMapping("/documents/create")
    public ResponseEntity<?> createDocumentResource(@RequestBody DocumentRequest request) {
        return ResponseEntity.ok(resourceService.createDocumentResource(request));
    }

    // Get all documents in a module
    @GetMapping("/modules/{moduleUuid}/documents")
    public ResponseEntity<?> getDocumentsByModule(@PathVariable UUID moduleUuid) {
        return ResponseEntity.ok(resourceService.getDocumentsByModule(moduleUuid));
    }

    // Get specific document resource
    @GetMapping("/documents/{documentUuid}")
    public ResponseEntity<DocumentResponseDTO> getDocumentResource(@PathVariable UUID documentUuid) {
        Resource resource = resourceService.getDocumentResource(documentUuid);
        Document document = resource.getDocument();
        return ResponseEntity.ok(resourceService.toDocumentResponseDTO(document));
    }

    // Download document file
    @GetMapping("/documents/{documentUuid}/download")
    public ResponseEntity<?> downloadDocument(@PathVariable UUID documentUuid) {
        try {
            Resource resource = resourceService.getDocumentResource(documentUuid);
            byte[] fileData = fileUploadService.getFile(resource.getDocument().getFilename(), "document");
            
            ByteArrayResource byteArrayResource = new ByteArrayResource(fileData);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getDocument().getTitle() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArrayResource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // View document (for PDFs and text files)
    @GetMapping("/documents/{documentUuid}/view")
    public ResponseEntity<?> viewDocument(@PathVariable UUID documentUuid) {
        try {
            Resource resource = resourceService.getDocumentResource(documentUuid);
            byte[] fileData = fileUploadService.getFile(resource.getDocument().getFilename(), "document");
            
            ByteArrayResource byteArrayResource = new ByteArrayResource(fileData);
            
            // Determine content type based on file type
            String contentType = getContentType(resource.getDocument().getFileType());
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(byteArrayResource);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Update document resource
    @PutMapping("/documents/{documentUuid}")
    public ResponseEntity<?> updateDocumentResource(@PathVariable UUID documentUuid, @RequestBody DocumentRequest request) {
        return ResponseEntity.ok(resourceService.updateDocumentResource(documentUuid, request));
    }

    // Delete document resource
    @DeleteMapping("/documents/{documentUuid}")
    public ResponseEntity<?> deleteDocumentResource(@PathVariable UUID documentUuid) {
        resourceService.deleteDocumentResource(documentUuid);
        return ResponseEntity.ok("Document resource deleted successfully.");
    }

    // Helper method to determine content type
    private String getContentType(String fileType) {
        switch (fileType.toUpperCase()) {
            case "PDF":
                return "application/pdf";
            case "DOC":
                return "application/msword";
            case "DOCX":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "TXT":
                return "text/plain";
            default:
                return "application/octet-stream";
        }
    }
} 