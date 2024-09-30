package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AsesorRequest implements Serializable {

    @NotEmpty(message = "El nombre no puede estar vacío")
    @JsonProperty("name")
    @Schema(example = "John Doe", description = "Nombre del asesor")
    private String name;

    @NotEmpty(message = "La descripción no puede estar vacía")
    @JsonProperty("description")
    @Schema(example = "Experto en desarrollo de software con 10 años de experiencia", 
            description = "Descripción del asesor")
    private String description;

    @NotNull(message = "El ID del área no puede ser nulo")
    @JsonProperty("id_area")
    @Schema(example = "1", description = "ID del área a la que pertenece el asesor")
    private Long idArea;

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

    public Long getIdArea() {
        return idArea;
    }

    public void setIdArea(Long idArea) {
        this.idArea = idArea;
    }
}