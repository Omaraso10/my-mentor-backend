package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa un área de especialización profesional")
public class AreaDto implements Serializable {

    @JsonProperty("id")
    @Schema(description = "Identificador único del área", example = "1")
    private Long id;

    @JsonProperty("name")
    @Schema(description = "Nombre del área", example = "Tecnología de la Información")
    private String name;

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

}
