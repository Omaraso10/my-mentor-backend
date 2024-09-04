package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;

import com.omar.backend.mymentor.models.IUser;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotEmpty;

@Schema(description = "DTO para la creación de un nuevo usuario")
public class UserCreateDto implements IUser, Serializable{

    @Schema(description = "Nombre del usuario", example = "John", required = true)
    @NotEmpty(message = "no puede estar vacio")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Contraseña del usuario", example = "password123", required = true)
    @NotEmpty(message = "no puede estar vacio")
    @JsonProperty("password")
    private String password;

    @Schema(description = "Número de teléfono del usuario", example = "1234567890")
    @JsonProperty("phone_number")
    private Long phoneNumber;

    @Schema(description = "Correo electrónico del usuario", example = "john.doe@example.com", required = true)
    @NotEmpty(message = "no puede estar vacio")
    @JsonProperty("email")
    private String email;

    @Schema(description = "Indica si el usuario tiene privilegios de administrador", example = "false")
    @JsonProperty("admin")
    private boolean admin;

    @Schema(description = "Indica si la cuenta del usuario está habilitada", example = "true")
    @JsonProperty("enabled")
    private boolean enabled;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public boolean isAdmin() {
        return admin;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    
    
}
