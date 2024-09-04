package com.omar.backend.mymentor.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omar.backend.mymentor.models.dto.AreaDto;
import com.omar.backend.mymentor.models.dto.MentorDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.services.ProfessionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(originPatterns = "*")
@Tag(name = "Professional", description = "API para gestionar profesionales, áreas y mentores")
public class ProfessionalController {

    @Autowired
    private ProfessionalService service;

    @Operation(summary = "Listar todas las áreas",
               description = "Obtiene una lista de todas las áreas disponibles")
    @ApiResponse(responseCode = "200", description = "Lista de áreas obtenida con éxito.",
                 content = @Content(schema = @Schema(implementation = AreaDto.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/areas")
    public ResponseEntity<?> listAreas() {
        List<AreaDto> areasDTO = null;
        Map<String, Object> response = new HashMap<>();
        try {
            areasDTO = service.findAreaAll();
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("areas", areasDTO);
        response.put("mensaje" , "Lista de áreas obtenida con éxito.");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Operation(summary = "Obtener área por ID",
               description = "Retorna un área específica basada en su ID")
    @ApiResponse(responseCode = "200", description = "Área encontrada con éxito.",
                 content = @Content(schema = @Schema(implementation = AreaDto.class)))
    @ApiResponse(responseCode = "404", description = "Área no encontrada en la BD.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/area/{id}")
    public ResponseEntity<?> showArea(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AreaDto> areaOptional = service.findAreaById(id);
            if (areaOptional.isPresent()) {
                response.put("area", areaOptional.orElseThrow());
                response.put("mensaje" , "Área encontrada con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Área no encontrada en la BD.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Listar todos los profesionales",
               description = "Obtiene una lista de todos los profesionales registrados")
    @ApiResponse(responseCode = "200", description = "Lista de profesionales obtenida con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron profesionales en la BD.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/professionals")
    public ResponseEntity<?> listProfessionals() {
        List<ProfessionalDto> professionalsDTO = null;
        Map<String, Object> response = new HashMap<>();
        try {
            professionalsDTO = service.findProfessionalAll();
            if(!professionalsDTO.isEmpty()){
                response.put("profesiones", professionalsDTO);
                response.put("mensaje" , "Lista de profesionales obtenida con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            }else{
                response.put("mensaje", "No se encontraron profesionales en la BD.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @Operation(summary = "Listar profesionales por área",
               description = "Obtiene una lista de profesionales filtrados por un área específica")
    @ApiResponse(responseCode = "200", description = "Lista de profesionales obtenida con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron profesionales para el área especificada.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/professionals/area/{areaId}")
    public ResponseEntity<?> listProfessionalsByArea(@PathVariable Long areaId) {
        List<ProfessionalDto> professionalsDTO = null;
        Map<String, Object> response = new HashMap<>();
        try {
            professionalsDTO = service.findProfessionalByAreaId(areaId);
            if(!professionalsDTO.isEmpty()){
                response.put("profesiones", professionalsDTO);
                response.put("mensaje" , "Lista de profesionales obtenida con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "No se encontraron profesionales para el área especificada.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }    
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener profesional por ID",
               description = "Retorna un profesional específico basado en su ID")
    @ApiResponse(responseCode = "200", description = "Profesional encontrado con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "404", description = "Profesional no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/professional/{id}")
    public ResponseEntity<?> showProfessional(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ProfessionalDto> areaOptional = service.findProfessionalById(id);
            if (areaOptional.isPresent()) {
                response.put("profesion", areaOptional.orElseThrow());
                response.put("mensaje" , "Profesional encontrado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Profesional no encontrado.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Agregar profesional a usuario",
               description = "Asocia un profesional a un usuario específico")
    @ApiResponse(responseCode = "201", description = "Profesional agregado con éxito.",
                 content = @Content(schema = @Schema(implementation = MentorDto.class)))
    @ApiResponse(responseCode = "400", description = "Error al asociar el profesional al usuario.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @PostMapping("/professional/{professionalId}/uuid/{uuid}")
    public ResponseEntity<?> addProfessionalToUser(@PathVariable String uuid, @PathVariable Long professionalId) {
        Map<String, Object> response = new HashMap<>();
        Optional<MentorDto> mentorOptional = null;
        try {
            mentorOptional = service.addProfessionalToUser(uuid, professionalId);
            if(mentorOptional.isPresent()){
                response.put("asesor", mentorOptional.orElseThrow());
                response.put("mensaje", "Profesional agregado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
            }else{
                response.put("mensaje", "Error al asociar el profesional al usuario.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar profesional de usuario",
               description = "Desasocia un profesional de un usuario")
    @ApiResponse(responseCode = "204", description = "Profesional eliminado con éxito.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @DeleteMapping("/mentor/{userProfessionalId}")
    public ResponseEntity<?> removeProfessionalFromUser(@PathVariable Long userProfessionalId) {
        Map<String, Object> response = new HashMap<>();
        try {
            service.removeProfessionalFromUser(userProfessionalId);
            response.put("mensaje", "Profesional eliminado con éxito.");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener mentor por ID",
               description = "Retorna un mentor específico basado en su ID")
    @ApiResponse(responseCode = "200", description = "Mentor encontrado con éxito.",
                 content = @Content(schema = @Schema(implementation = MentorDto.class)))
    @ApiResponse(responseCode = "404", description = "Mentor no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/mentor/{id}")
    public ResponseEntity<?> showMentor(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<MentorDto> mentorOptional = service.findMentorById(id);
            if (mentorOptional.isPresent()) {
                response.put("asesor", mentorOptional.orElseThrow());
                response.put("mensaje" , "Mentor encontrado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Mentor no encontrado.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
