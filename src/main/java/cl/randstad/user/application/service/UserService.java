package cl.randstad.user.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.randstad.common.exception.EmailException;
import cl.randstad.common.exception.UserException;
import cl.randstad.common.security.JwtTokenService;
import cl.randstad.user.infraestructura.persistence.UserJpaRepository;
import cl.randstad.user.infraestructura.persistence.entity.UserEntity;
import cl.randstad.user.infraestructura.persistence.mapper.UserMapper;
import cl.randstad.user.infraestructura.rest.dto.UserRequest;
import cl.randstad.user.infraestructura.rest.dto.UserResponse;
import cl.randstad.user.infraestructura.rest.mapper.UserDtoMapper;

@Service
public class UserService implements UserServicePort{
	private final UserJpaRepository userRepo;
    private final JwtTokenService jwtService;
    private final UserMapper userMapper;
    private final UserDtoMapper userDtoMapper;

	public UserService(UserJpaRepository userRepo, JwtTokenService jwtService, UserMapper userMapper,
			UserDtoMapper userDtoMapper) {
		this.userRepo = userRepo;
		this.jwtService = jwtService;
		this.userMapper = userMapper;
		this.userDtoMapper = userDtoMapper;
	}

	@Transactional
	@Override
	public UserResponse createUser(UserRequest request) {
        userRepo.findByEmail(request.getEmail()).ifPresent(u -> {
            throw new EmailException("El correo " + request.getEmail() + ", ya está registrado ");
        });
        String token = jwtService.generateToken(UUID.randomUUID());
        UserEntity userEntity = userMapper.toEntity(request, UUID.randomUUID(), token, LocalDateTime.now());
        
        UserEntity entity =  userRepo.save(userEntity);
        return userDtoMapper.toResponseSucces(entity);
    }

	@Transactional
	@Override
	public UserResponse update(UUID id, UserRequest request) {
		UserEntity existing = userRepo.findById(id)
		        .orElseThrow(() -> new UserException("Usuario no encontrado"));
		
		userDtoMapper.mapUpdate(existing, request);

	    UserEntity entity = userRepo.save(existing);
	    return userDtoMapper.toResponseSucces(entity);
	}
	
	/**
	 * Eliminacion Fisica
	 * 
	public void delete(UUID id){
	    UserEntity user = userRepo.findById(id)
	        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    userRepo.delete(user);
	}
	*/
	
	@Transactional
	@Override
	public void delete(UUID id) {
		UserEntity user = userRepo.findById(id)
		        .orElseThrow(() -> new UserException("Usuario no encontrado"));

	    user.setActive(false);
	    user.setModified(LocalDateTime.now());
	    userRepo.save(user);
	}


	@Transactional(readOnly = true)
	@Override
	public UserResponse findById(UUID id) {
	    UserEntity entity = userRepo.findById(id)
	        .orElseThrow(() -> new UserException("Usuario no encontrado: " + id));
	    
	    return userDtoMapper.toResponse(entity);
	}


	@Transactional(readOnly = true)
	@Override
	public List<UserResponse> findAll() {
		return userRepo.findAll().stream()
	            .map(userDtoMapper::toResponse)
	            .collect(Collectors.toList());
    }
}
