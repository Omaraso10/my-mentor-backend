package com.omar.backend.mymentor.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.omar.backend.mymentor.models.dto.AdvisoryDto;
import com.omar.backend.mymentor.models.dto.AskProfessionalDto;
import com.omar.backend.mymentor.services.AdvisoryService;
import com.omar.backend.mymentor.services.AIService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gpt")
@CrossOrigin(origins = "${allowed.origins}")
@Tag(name = "ChatGPT Asesorías", description = "API para gestionar asesorías profesionales utilizando IA")
public class ChatGPTController {

    private final AIService aIService;
    private final AdvisoryService advisoryService;

    public ChatGPTController(AIService aIService, AdvisoryService advisoryService) {
        this.aIService = aIService;
        this.advisoryService = advisoryService;
    }

    @Operation(summary = "Obtener lista de asesorías por profesional", 
                description = "Retorna una lista de asesorías asociadas al profesional seleccionado")
    @ApiResponse(responseCode = "200", description = "Lista de asesorías encontrada con éxito.",
    content = @Content(schema = @Schema(implementation = AdvisoryDto.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron registros en la base de datos.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud.")
    @GetMapping("/professional/{id}")
    public ResponseEntity<?> listAdvisorys(@PathVariable Long id) {
        List<AdvisoryDto> advisoryDtos = null;
        Map<String, Object> response = new HashMap<>();
        try {
            advisoryDtos = advisoryService.findByUserProfessionalId(id);
            if(!advisoryDtos.isEmpty()){
                response.put("advisorys", advisoryDtos);
                response.put("mensaje" , "Lista de asesorías encontrada con éxito.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                response.put("mensaje", "No se encontraron registros en la base de datos.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor al procesar la solicitud.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener la asesoría por ID", 
                description = "Retorna una asesoría específica basada en su ID")
    @ApiResponse(responseCode = "200", description = "Asesoría encontrada con éxito.",
    content = @Content(schema = @Schema(implementation = AdvisoryDto.class)))
    @ApiResponse(responseCode = "404", description = "No se encontró la asesoría con el ID especificado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud.")
    @GetMapping("/professional/advice/{id}")
    public ResponseEntity<?> showAdvice(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AdvisoryDto> areaOptional = advisoryService.findAdvisoryById(id);
            if (areaOptional.isPresent()) {
                response.put("advice", areaOptional.orElseThrow());
                response.put("mensaje" , "Asesoría encontrada con éxito.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "No se encontró la asesoría con el ID especificado.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor al procesar la solicitud.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Crear una nueva asesoría", 
               description = "Crea una nueva asesoría basada en la pregunta del usuario y la respuesta del profesional")
    @ApiResponse(responseCode = "201", description = "Asesoría creada exitosamente.",
                 content = @Content(schema = @Schema(implementation = AdvisoryDto.class)))
    @ApiResponse(responseCode = "400", description = "Datos de la asesoría inválidos o pregunta vacía.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud.")
    @PostMapping("/professional/advice")
    public ResponseEntity<?> addAdvice(@RequestBody AskProfessionalDto ask) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AdvisoryDto> advisoryOptional = aIService.addAdviceIA(ask);
            
            if (advisoryOptional.isPresent()) {
                response.put("advice", advisoryOptional.orElseThrow());
                response.put("mensaje", "Asesoría creada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("mensaje", "Datos de la asesoría inválidos o pregunta vacía.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            response.put("mensaje", "Error al procesar la solicitud.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar una asesoría existente", 
               description = "Actualiza una asesoría existente identificada por su ID con nueva información")
    @ApiResponse(responseCode = "200", description = "Asesoría actualizada exitosamente.",
                 content = @Content(schema = @Schema(implementation = AdvisoryDto.class)))
    @ApiResponse(responseCode = "400", description = "Datos de la asesoría inválidos o ID no existente.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud.")
    @PutMapping("/professional/advice/{id}")
    public ResponseEntity<?> updateAdvice(@RequestBody AskProfessionalDto ask, @PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AdvisoryDto> advisoryOptional = aIService.updateAdviceIA(ask, id);
            
            if (advisoryOptional.isPresent()) {
                response.put("advice", advisoryOptional.orElseThrow());
                response.put("mensaje", "Asesoría actualizada exitosamente.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Datos de la asesoría inválidos o ID no existente.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            response.put("mensaje", "Error al procesar la solicitud.");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar una asesoría", 
               description = "Elimina una asesoría existente identificada por su ID.")
    @ApiResponse(responseCode = "204", description = "Asesoría eliminada exitosamente.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la solicitud.")
    @DeleteMapping("/professional/advice/{id}")
    public ResponseEntity<?> removeAdvice(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            advisoryService.removeAdvisoryById(id);
            response.put("mensaje", "Asesoría eliminada exitosamente.");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("mensaje", "Error interno del servidor al procesar la solicitud.");
            response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}