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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.omar.backend.mymentor.models.dto.AreaDto;
import com.omar.backend.mymentor.models.dto.AsesorRequest;
import com.omar.backend.mymentor.models.dto.MentorDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.services.ProfessionalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "${allowed.origins}")
@Tag(name = "Professional", description = "API para gestionar asesores, áreas y mentores")
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

    @Operation(summary = "Listar todos los asesores",
               description = "Obtiene una lista de todos los asesores registrados")
    @ApiResponse(responseCode = "200", description = "Lista de asesores obtenida con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron asesores en la BD.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/professionals")
    public ResponseEntity<?> listProfessionals() {
        List<ProfessionalDto> professionalsDTO = null;
        Map<String, Object> response = new HashMap<>();
        try {
            professionalsDTO = service.findProfessionalAll();
            if(!professionalsDTO.isEmpty()){
                response.put("profesiones", professionalsDTO);
                response.put("mensaje" , "Lista de asesores obtenida con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            }else{
                response.put("mensaje", "No se encontraron asesores en la BD.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }

    @Operation(summary = "Listar asesores por área",
               description = "Obtiene una lista de asesores filtrados por un área específica")
    @ApiResponse(responseCode = "200", description = "Lista de asesores obtenida con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "404", description = "No se encontraron asesores para el área especificada.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/professionals/area/{areaId}")
    public ResponseEntity<?> listProfessionalsByArea(@PathVariable Long areaId) {
        List<ProfessionalDto> professionalsDTO = null;
        Map<String, Object> response = new HashMap<>();
        try {
            professionalsDTO = service.findProfessionalByAreaId(areaId);
            if(!professionalsDTO.isEmpty()){
                response.put("profesiones", professionalsDTO);
                response.put("mensaje" , "Lista de asesores obtenida con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "No se encontraron asesores para el área especificada.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }    
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener asesor por ID",
               description = "Retorna un asesor específico basado en su ID")
    @ApiResponse(responseCode = "200", description = "asesor encontrado con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "404", description = "asesor no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/professional/{id}")
    public ResponseEntity<?> showProfessional(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ProfessionalDto> professionalOptional = service.findProfessionalById(id);
            if (professionalOptional.isPresent()) {
                response.put("profesion", professionalOptional.orElseThrow());
                response.put("mensaje" , "Asesor encontrado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Asesor no encontrado.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Crear un nuevo asesor",
               description = "Crea un nuevo asesor con los datos proporcionados")
    @ApiResponse(responseCode = "201", description = "Asesor creado con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "400", description = "Error en los datos proporcionados.")
    @ApiResponse(responseCode = "404", description = "Área no encontrada.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @PostMapping("/professional")
    public ResponseEntity<?> createProfessional(@Valid @RequestBody AsesorRequest asesorRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AreaDto> areaOptional = service.findAreaById(asesorRequest.getIdArea());
            if (!areaOptional.isPresent()) {
                response.put("mensaje", "El área con ID: " + asesorRequest.getIdArea() + " no existe.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            ProfessionalDto professionalDto = new ProfessionalDto();
            professionalDto.setName(asesorRequest.getName());
            professionalDto.setDescription(asesorRequest.getDescription());
            professionalDto.setArea(areaOptional.get());

            ProfessionalDto createdProfessional = service.createProfessional(professionalDto);

            response.put("asesor", createdProfessional);
            response.put("mensaje", "Asesor creado con éxito.");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al crear el asesor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Actualizar un asesor existente",
               description = "Actualiza los datos de un asesor existente")
    @ApiResponse(responseCode = "200", description = "Asesor actualizado con éxito.",
                 content = @Content(schema = @Schema(implementation = ProfessionalDto.class)))
    @ApiResponse(responseCode = "404", description = "Asesor o área no encontrados.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @PutMapping("/professional/{id}")
    public ResponseEntity<?> updateProfessional(@PathVariable Long id, @Valid @RequestBody AsesorRequest asesorRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<AreaDto> areaOptional = service.findAreaById(asesorRequest.getIdArea());
            if (!areaOptional.isPresent()) {
                response.put("mensaje", "El área con ID: " + asesorRequest.getIdArea() + " no existe.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            ProfessionalDto professionalDto = new ProfessionalDto();
            professionalDto.setId(id);
            professionalDto.setName(asesorRequest.getName());
            professionalDto.setDescription(asesorRequest.getDescription());
            professionalDto.setArea(areaOptional.get());

            Optional<ProfessionalDto> updatedProfessionalOptional = service.updateProfessional(id, professionalDto);

            if (updatedProfessionalOptional.isPresent()) {
                response.put("asesor", updatedProfessionalOptional.get());
                response.put("mensaje", "Asesor actualizado con éxito.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "No se encontró el asesor con ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al actualizar el asesor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar un asesor",
               description = "Elimina un asesor existente por su ID")
    @ApiResponse(responseCode = "204", description = "Asesor eliminado con éxito.")
    @ApiResponse(responseCode = "404", description = "Asesor no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @DeleteMapping("/professional/{id}")
    public ResponseEntity<?> deleteProfessional(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<ProfessionalDto> professionalOptional = service.findProfessionalById(id);
            if (professionalOptional.isPresent()) {
                service.deleteProfessional(id);
                response.put("mensaje", "Asesor eliminado con éxito.");
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            } else {
                response.put("mensaje", "No se encontró el asesor con ID: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al eliminar el asesor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Agregar asesor a usuario",
               description = "Asocia un asesor a un usuario específico")
    @ApiResponse(responseCode = "201", description = "Asesor agregado con éxito.",
                 content = @Content(schema = @Schema(implementation = MentorDto.class)))
    @ApiResponse(responseCode = "400", description = "Error al asociar el asesor al usuario.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @PostMapping("/professional/{professionalId}/uuid/{uuid}")
    public ResponseEntity<?> addProfessionalToUser(@PathVariable String uuid, @PathVariable Long professionalId) {
        Map<String, Object> response = new HashMap<>();
        Optional<MentorDto> mentorOptional = null;
        try {
            mentorOptional = service.addProfessionalToUser(uuid, professionalId);
            if(mentorOptional.isPresent()){
                response.put("asesor", mentorOptional.orElseThrow());
                response.put("mensaje", "Asesor agregado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
            }else{
                response.put("mensaje", "Error al asociar el asesor al usuario.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Eliminar asesor de usuario",
               description = "Desasocia un asesor de un usuario")
    @ApiResponse(responseCode = "204", description = "Asesor eliminado con éxito.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @DeleteMapping("/mentor/{userProfessionalId}")
    public ResponseEntity<?> removeProfessionalFromUser(@PathVariable Long userProfessionalId) {
        Map<String, Object> response = new HashMap<>();
        try {
            service.removeProfessionalFromUser(userProfessionalId);
            response.put("mensaje", "Asesor eliminado con éxito.");
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
