package cl.randstad.user.infraestructura.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.randstad.user.application.service.UserServicePort;
import cl.randstad.user.infraestructura.rest.dto.UserRequest;
import cl.randstad.user.infraestructura.rest.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Operaciones Crud para los usuarios")
public class UserController {
	
	private final UserServicePort service;

    public UserController(UserServicePort service) {
		this.service = service;
	}
	

	@PostMapping
	@Operation(summary = "Crear usuario")
    public ResponseEntity<UserResponse> insert(@RequestBody @Valid UserRequest userRequest) {
		
		UserResponse userResponse = service.createUser(userRequest);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @RequestBody UserRequest userRequest) {
    	UserResponse updatedUser = service.update(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public ResponseEntity<List<UserResponse>> listar() {
    	return ResponseEntity.ok(service.findAll());
    }	 

    @GetMapping("/{id}")
    @Operation(summary = "Buscar a un usuario por su id")
    public ResponseEntity<UserResponse> obtener(@PathVariable("id") UUID id) {
    	UserResponse usuario = service.findById(id);
        return ResponseEntity.ok(usuario);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario de forma logica")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
    	service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

