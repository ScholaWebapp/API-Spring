package com.scholaapi.service;

import com.scholaapi.model.User;
import com.scholaapi.repository.AccountRepository;
import com.scholaapi.repository.UserRepository;
import com.scholaapi.repository.OrganizationRepository;
import com.scholaapi.repository.CourseEnrollmentRepository;
import com.scholaapi.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private OrganizationRepository organizationRepository;
    
    @Autowired
    private CourseEnrollmentRepository courseEnrollmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;

    public List<User> fetchAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void delete(UUID uuid) {
        // Delete all course enrollments for this user
        courseEnrollmentRepository.deleteByUserUuid(uuid);
        
        // Delete all courses where this user is professor (this will cascade to modules, resources, enrollments)
        courseRepository.deleteByProfessorUuid(uuid);
        
        // Delete all organizations created by this user (this will cascade to courses, modules, resources, enrollments)
        organizationRepository.deleteByUserUuid(uuid);
        
        // Delete the user's account
        accountRepository.deleteByUserUuid(uuid);
        
        // Finally delete the user
        userRepository.deleteByUuid(uuid);
    }
}
