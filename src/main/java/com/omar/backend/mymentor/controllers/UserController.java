package com.omar.backend.mymentor.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omar.backend.mymentor.models.dto.UserCreateDto;
import com.omar.backend.mymentor.models.dto.UserResponseDTO;
import com.omar.backend.mymentor.models.dto.UserUpdateDto;
import com.omar.backend.mymentor.services.UserService;
import com.omar.backend.mymentor.util.Helper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "${allowed.origins}")
@Tag(name = "User", description = "API para gestionar usuarios")
public class UserController {
    
    @Autowired
    private UserService service;

    @Operation(summary = "Listar todos los usuarios",
               description = "Obtiene una lista de todos los usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida con éxito.",
                 content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "500", description = "Error al realizar la consulta en la base de datos.")
    @GetMapping
    public ResponseEntity<?> list() {
        List<UserResponseDTO> usersDTO = null;
        Map<String, Object> response = new HashMap<>();
        try {
            usersDTO = service.findAll();
        } catch(DataAccessException e){
            response.put("mensaje", "Error al realizar la consulta en la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("usuarios", usersDTO);
        response.put("mensaje" , "Lista de usuarios obtenida con éxito.");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

    @Operation(summary = "Listar usuarios paginados",
               description = "Obtiene una página de usuarios")
    @ApiResponse(responseCode = "200", description = "Página de usuarios obtenida con éxito.",
                 content = @Content(schema = @Schema(implementation = Page.class)))
    @GetMapping("/page/{page}")
    public Page<UserResponseDTO> list(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 5);
        return service.findAll(pageable);
    }

    @Operation(summary = "Obtener usuario por UUID",
               description = "Retorna un usuario específico basado en su UUID")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito.",
                 content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/{uuid}")
    public ResponseEntity<?> show(@PathVariable String uuid) {
        Optional<UserResponseDTO> userOptional = service.findByUuid(uuid);
        Map<String, Object> response = new HashMap<>();
        try {
            if (userOptional.isPresent()) {
                response.put("usuario", userOptional.orElseThrow());
                response.put("mensaje" , "Usuario encontrado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Usuario no encontrado.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Obtener usuario por Email",
               description = "Retorna un usuario específico basado en su Email")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado con éxito.",
                 content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> showByEmail(@PathVariable String email) {
        Optional<UserResponseDTO> userOptional = service.findByEmail(email);
        Map<String, Object> response = new HashMap<>();
        try {
            if (userOptional.isPresent()) {
                response.put("usuario", userOptional.orElseThrow());
                response.put("mensaje" , "Usuario encontrado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Usuario no encontrado.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch(DataAccessException e){
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(summary = "Crear un nuevo usuario",
               description = "Crea un nuevo usuario en el sistema")
    @ApiResponse(responseCode = "201", description = "Usuario creado con éxito.",
                 content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateDto userDto, BindingResult result) {
        UserResponseDTO userNew;
        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors()){
            return validation(result);
        }
        if(!Helper.isValidPassword(userDto.getPassword())){
            response.put("mensaje", "La password no cumple con las políticas de seguridad.");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        if(!Helper.isValidEmail(userDto.getEmail())){
            response.put("mensaje", "El mail ingresado no es una dirección de correo valida");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            if(service.existsByEmail(userDto.getEmail())){
                response.put("mensaje", "Error al crear el usuario en la base de datos.");
                response.put("error", "El correo ya se encuentra registrado en la base de datos!");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            userNew = service.save(userDto);
        } catch (DataAccessException e) {
            response.put("mensaje", "Error al crear el usuario en la base de datos.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "Usuario creado con éxito.");
        response.put("usuario", userNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un usuario existente",
               description = "Actualiza los datos de un usuario existente basado en su UUID")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito.",
                 content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @PutMapping("/{uuid}")
    public ResponseEntity<?> update(@Valid @RequestBody UserUpdateDto userDto, BindingResult result, @PathVariable String uuid) {
        Map<String, Object> response = new HashMap<>();
        if(result.hasErrors()){
            return validation(result);
        }
        try {
            Optional<UserResponseDTO> o = service.update(userDto, uuid);
            if (o.isPresent()) {
                response.put("mensaje", "Usuario actualizado con éxito.");
                response.put("usuario", o.orElseThrow());
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
            } else {
                response.put("mensaje", "Usuario no encontrado.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            response.put("mensaje", "Error interno del servidor.");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Operation(summary = "Eliminar un usuario",
               description = "Elimina un usuario existente basado en su UUID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito.")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<?> remove(@PathVariable String uuid) {
        Map<String, Object> response = new HashMap<>();
        Optional<UserResponseDTO> o = service.findByUuid(uuid);
        if (o.isPresent()) {
            try {
                service.remove(uuid);
                response.put("mensaje", "Usuario eliminado con éxito.");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
            } catch (DataAccessException e) {
                response.put("mensaje", "Error interno del servidor.");
                response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            response.put("mensaje", "Usuario no encontrado.");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
    }
    
    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();

        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
