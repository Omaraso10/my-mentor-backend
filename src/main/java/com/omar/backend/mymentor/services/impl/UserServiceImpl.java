package com.omar.backend.mymentor.services.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.backend.mymentor.models.IUser;
import com.omar.backend.mymentor.models.dto.UserCreateDto;
import com.omar.backend.mymentor.models.dto.UserResponseDTO;
import com.omar.backend.mymentor.models.dto.UserUpdateDto;
import com.omar.backend.mymentor.models.dto.mapper.DtoMapperUserCreateRequest;
import com.omar.backend.mymentor.models.dto.mapper.DtoMapperUserResponse;
import com.omar.backend.mymentor.models.entities.Role;
import com.omar.backend.mymentor.models.entities.User;
import com.omar.backend.mymentor.repositories.RoleRepository;
import com.omar.backend.mymentor.repositories.UserRepository;
import com.omar.backend.mymentor.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        List<User> users = (List<User>) repository.findAll();
        return users
                .stream()
                .map(u -> DtoMapperUserResponse.builder().setUser(u).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return repository
                .findAll(pageable)
                .map(u -> DtoMapperUserResponse.builder().setUser(u).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findById(Long id) {
        return repository.findById(id).map(u -> DtoMapperUserResponse
                .builder()
                .setUser(u)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findByUuid(String id) {
        return repository.findByUuid(id).map(u -> DtoMapperUserResponse
                .builder()
                .setUser(u)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findByEmail(String email) {
        return repository.getUserByEmail(email).map(u -> DtoMapperUserResponse
                .builder()
                .setUser(u)
                .build());
    }

    @Override
    @Transactional
    public UserResponseDTO save(UserCreateDto userDto) {
        User user = DtoMapperUserCreateRequest.builder().setUser(userDto).build();
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(getRoles(user));
        return DtoMapperUserResponse.builder().setUser(repository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserResponseDTO> update(UserUpdateDto userDto, String uuid) {
        Optional<User> o = repository.findByUuid(uuid);
        User userOptional = null;
        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            LocalDate today = LocalDate.now();
            userDb.setModified(today);
            userDb.setRoles(getRoles(userDto));
            userDb.setName(userDto.getName());
            userDb.setLastName(userDto.getLastName());
            userDb.setPhoneNumber(userDto.getPhoneNumber());
            userDb.setEnabled(userDto.isEnabled());
            userOptional = repository.save(userDb);
        }
        return Optional.ofNullable(DtoMapperUserResponse.builder().setUser(userOptional).build());
    }

    @Override
    @Transactional
    public void remove(String uuid) {
        repository.deleteByUuid(uuid);
    }

    private List<Role> getRoles(IUser user) {
        Optional<Role> ou = roleRepository.findByName("ROLE_USER");

        List<Role> roles = new ArrayList<>();
        if (ou.isPresent()) {
            roles.add(ou.orElseThrow());
        }

        if (user.isAdmin()) {
            Optional<Role> oa = roleRepository.findByName("ROLE_ADMIN");
            if (oa.isPresent()) {
                roles.add(oa.orElseThrow());
            }
        }
        return roles;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

}
