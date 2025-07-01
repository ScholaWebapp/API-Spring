package com.scholaapi.service;

import com.scholaapi.dto.ModuleRequest;
import com.scholaapi.dto.ModuleResponseDTO;
import com.scholaapi.dto.ResourceResponseDTO;
import com.scholaapi.model.Course;
import com.scholaapi.model.Module;
import com.scholaapi.model.Resource;
import com.scholaapi.repository.CourseRepository;
import com.scholaapi.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Get all modules in a course
    public List<ModuleResponseDTO> getModulesByCourse(UUID courseUuid) {
        if (!courseRepository.existsById(courseUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        List<Module> modules = moduleRepository.findByCourse_UuidOrderByOrderIndexAsc(courseUuid);
        return modules.stream().map(this::toModuleDTO).toList();
    }

    public ModuleResponseDTO toModuleDTO(Module module) {
        ModuleResponseDTO dto = new ModuleResponseDTO();
        dto.setUuid(module.getUuid());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setResources(module.getResources().stream().map(this::toResourceDTO).toList());
        return dto;
    }

    private ResourceResponseDTO toResourceDTO(Resource resource) {
        ResourceResponseDTO dto = new ResourceResponseDTO();
        dto.setUuid(resource.getUuid());
        dto.setResourceType(resource.getResourceType().name());
        dto.setTitle(resource.getTitle());
        dto.setDescription(resource.getDescription());
        if (resource.getVideo() != null) {
            dto.setVideoFilename(resource.getVideo().getFilename());
        }
        if (resource.getDocument() != null) {
            dto.setDocumentFilename(resource.getDocument().getFilename());
        }
        return dto;
    }

    // Get specific module
    public Module getModuleById(UUID moduleUuid) {
        return moduleRepository.findById(moduleUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));
    }

    // Create module
    @Transactional
    public Module createModule(UUID courseUuid, ModuleRequest request) {
        Course course = courseRepository.findById(courseUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        List<Module> existingModules = moduleRepository.findByCourse_UuidOrderByOrderIndexAsc(courseUuid);
        int nextOrderIndex = existingModules.size() + 1;

        Module module = new Module();
        module.setCourse(course);
        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setOrderIndex(nextOrderIndex);

        return moduleRepository.save(module);
    }

    // Update module
    @Transactional
    public Module updateModule(UUID moduleUuid, ModuleRequest request) {
        Module module = moduleRepository.findById(moduleUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));

        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        // Keep existing orderIndex - don't allow changing it through update

        return moduleRepository.save(module);
    }

    // Delete module
    @Transactional
    public void deleteModule(UUID moduleUuid) {
        if (!moduleRepository.existsById(moduleUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found");
        }
        moduleRepository.deleteById(moduleUuid);
    }
} 