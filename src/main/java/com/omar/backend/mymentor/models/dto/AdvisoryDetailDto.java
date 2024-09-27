package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa los detalles de una asesoría, incluyendo la pregunta, la respuesta y la imagen en base64")
public class AdvisoryDetailDto implements Serializable {

    @JsonProperty("id")
    @Schema(description = "Identificador único del detalle de la asesoría", 
            example = "1", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @JsonProperty("line_number")
    @Schema(description = "Número de línea o secuencia del detalle dentro de la asesoría", 
            example = "1", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long lineNumber;

    @JsonProperty("question")
    @Schema(description = "Pregunta realizada por el usuario", 
            example = "¿Cuáles son las mejores prácticas para el desarrollo de software?", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String question;

    @JsonProperty("answer")
    @Schema(description = "Respuesta proporcionada por el sistema o profesional", 
            example = "Las mejores prácticas para el desarrollo de software incluyen: 1. Escribir código limpio y legible. 2. Realizar pruebas unitarias y de integración. 3. Utilizar control de versiones. 4. Implementar revisiones de código. 5. Seguir principios SOLID. 6. Documentar el código y las APIs. 7. Optimizar el rendimiento. 8. Mantener la seguridad como prioridad.", 
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String answer;

    @JsonProperty("model")
    @Schema(example = "gpt-3.5-turbo", description = "Modelo de IA utilizado para la asesoría")
    private String model;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}