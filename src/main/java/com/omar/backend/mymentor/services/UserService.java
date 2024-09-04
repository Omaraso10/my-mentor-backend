package com.omar.backend.mymentor.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.omar.backend.mymentor.models.dto.UserCreateDto;
import com.omar.backend.mymentor.models.dto.UserResponseDTO;
import com.omar.backend.mymentor.models.dto.UserUpdateDto;

public interface UserService {
    
    List<UserResponseDTO> findAll();
    
    Page<UserResponseDTO> findAll(Pageable pageable);

    Optional<UserResponseDTO> findById(Long id);

    Optional<UserResponseDTO> findByUuid(String uuid);

    Optional<UserResponseDTO> findByEmail(String email);

    UserResponseDTO save(UserCreateDto user);

    Optional<UserResponseDTO> update(UserUpdateDto user, String uuid);

    void remove(String uuid);

    public boolean existsByEmail(String email);
    
}
