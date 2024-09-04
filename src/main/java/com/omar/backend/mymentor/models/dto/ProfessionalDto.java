package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a un profesional en el sistema")
public class ProfessionalDto implements Serializable {

    @JsonProperty("id")
    @Schema(description = "Identificador único del profesional", example = "1")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Nombre del profesional", example = "Desarrollador Mobile")
    private String name;

    @JsonProperty("description")
    @Schema(description = "Descripción breve del profesional", 
            example = "Como Desarrollador Mobile, me especializo en la creación de aplicaciones para dispositivos móviles que son intuitivas...")
    private String description;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}
