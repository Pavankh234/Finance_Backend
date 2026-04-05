package com.finance.service;

import com.finance.domain.entity.Role;
import com.finance.domain.entity.User;
import com.finance.dto.request.CreateUserRequest;
import com.finance.dto.request.UpdateUserRequest;
import com.finance.dto.response.PageResponse;
import com.finance.dto.response.UserResponse;
import com.finance.exception.DuplicateResourceException;
import com.finance.exception.ResourceNotFoundException;
import com.finance.repository.RoleRepository;
import com.finance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("DESC") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> userPage = userRepository.findAll(pageable);
        return PageResponse.from(userPage, UserResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserResponse.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> searchUsers(String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.searchUsers(search, pageable);
        return PageResponse.from(userPage, UserResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getUsersByStatus(User.UserStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findByStatus(status, pageable);
        return PageResponse.from(userPage, UserResponse::fromEntity);
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) throw new DuplicateResourceException("User", "username", request.getUsername());
        if (userRepository.existsByEmail(request.getEmail())) throw new DuplicateResourceException("User", "email", request.getEmail());
        Set<Role> roles = new HashSet<>();
        for (Role.RoleName roleName : request.getRoles()) {
            Role role = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
            roles.add(role);
        }
        User user = User.builder().username(request.getUsername()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).fullName(request.getFullName()).roles(roles).status(User.UserStatus.ACTIVE).build();
        user = userRepository.save(user);
        log.info("Created user: {}", user.getUsername());
        return UserResponse.fromEntity(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) throw new DuplicateResourceException("User", "email", request.getEmail());
            user.setEmail(request.getEmail());
        }
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPassword() != null) user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (request.getStatus() != null) user.setStatus(request.getStatus());
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Role.RoleName roleName : request.getRoles()) {
                Role role = roleRepository.findByName(roleName).orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName));
                roles.add(role);
            }
            user.setRoles(roles);
        }
        user = userRepository.save(user);
        log.info("Updated user: {}", user.getUsername());
        return UserResponse.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
        log.info("Deleted user: {}", user.getUsername());
    }

    @Transactional
    public UserResponse updateUserStatus(Long id, User.UserStatus status) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setStatus(status);
        user = userRepository.save(user);
        log.info("Updated user {} status to {}", user.getUsername(), status);
        return UserResponse.fromEntity(user);
    }
}
