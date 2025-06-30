package com.scholaapi.service;

import com.scholaapi.dto.ModuleRequest;
import com.scholaapi.model.Course;
import com.scholaapi.model.Module;
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
    public List<Module> getModulesByCourse(UUID courseUuid) {
        if (!courseRepository.existsById(courseUuid)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }
        return moduleRepository.findByCourseUuidOrderByOrderIndexAsc(courseUuid);
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

        List<Module> existingModules = moduleRepository.findByCourseUuidOrderByOrderIndexAsc(courseUuid);
        int requestedOrderIndex = request.getOrderIndex() != null ? request.getOrderIndex() : existingModules.size() + 1;
        
        // Validate order index
        if (requestedOrderIndex < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order index must be at least 1");
        }
        
        // Shift existing modules to make room for the new one
        for (Module module : existingModules) {
            if (module.getOrderIndex() >= requestedOrderIndex) {
                module.setOrderIndex(module.getOrderIndex() + 1);
                moduleRepository.save(module);
            }
        }

        Module module = new Module();
        module.setCourse(course);
        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setOrderIndex(requestedOrderIndex);

        return moduleRepository.save(module);
    }

    // Update module
    @Transactional
    public Module updateModule(UUID moduleUuid, ModuleRequest request) {
        Module module = moduleRepository.findById(moduleUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Module not found"));

        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setOrderIndex(request.getOrderIndex());

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