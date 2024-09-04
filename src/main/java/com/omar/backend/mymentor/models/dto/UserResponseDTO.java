package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa la respuesta con información de un usuario")
public class UserResponseDTO implements Serializable{

    @Schema(description = "Identificador único del usuario", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty("uuid")
    private String uuid;

    @Schema(description = "Nombre del usuario", example = "John")
    @JsonProperty("name")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Número de teléfono del usuario", example = "1234567890")
    @JsonProperty("phone_number")
    private Long phoneNumber;

    @Schema(description = "Correo electrónico del usuario", example = "john.doe@example.com")
    @JsonProperty("email")
    private String email;

    @Schema(description = "Indica si el usuario tiene privilegios de administrador", example = "false")
    @JsonProperty("admin")
    private boolean admin;

    @Schema(description = "Indica si la cuenta del usuario está habilitada", example = "true")
    @JsonProperty("enabled")
    private boolean enabled;

    @Schema(description = "Lista de mentores asociados al usuario")
    @JsonProperty("asesores")
    private List<MentorDto> mentors;

    public String getUuid() {
        return uuid;
    }

    public UserResponseDTO() {
    }

    public UserResponseDTO(String uuid, String name, String lastName, Long phoneNumber, String email, boolean admin,
            boolean enabled, List<MentorDto> mentors) {
        this.uuid = uuid;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.admin = admin;
        this.enabled = enabled;
        this.mentors = mentors;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

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

    public List<MentorDto> getMentors() {
        return mentors;
    }

    public void setMentors(List<MentorDto> mentors) {
        this.mentors = mentors;
    }
    
}
