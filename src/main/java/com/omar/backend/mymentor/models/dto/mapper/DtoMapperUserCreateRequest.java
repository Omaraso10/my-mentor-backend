package com.omar.backend.mymentor.models.dto.mapper;

import org.springframework.stereotype.Component;

import com.omar.backend.mymentor.models.dto.UserCreateDto;
import com.omar.backend.mymentor.models.entities.User;

@Component
public class DtoMapperUserCreateRequest {

    private UserCreateDto userDto;
    
    private DtoMapperUserCreateRequest() {
    }

    public static DtoMapperUserCreateRequest builder() {
        return new DtoMapperUserCreateRequest();
    }

    public DtoMapperUserCreateRequest setUser(UserCreateDto userDTO) {
        this.userDto = userDTO;
        return this;
    }

    public User build() {
        if (userDto == null) {
            throw new RuntimeException("Debe pasar el entity user!");
        }
        User user = new User();
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setEmail(userDto.getEmail());
        user.setAdmin(userDto.isAdmin());
        user.setEnabled(userDto.isEnabled());
        return user;
    }

}
