package cl.randstad.user.infraestructura.persistence.mapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import cl.randstad.user.infraestructura.persistence.entity.PhoneEntity;
import cl.randstad.user.infraestructura.persistence.entity.UserEntity;
import cl.randstad.user.infraestructura.rest.dto.UserRequest;

@Component
public class UserMapper {
	public UserEntity toEntity(UserRequest request, UUID id, String token, LocalDateTime now) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setCreated(now);
        user.setModified(now);
        user.setLastLogin(now);
        user.setActive(true);
        user.setToken(token);

        List<PhoneEntity> phones = Optional.ofNullable(request.getPhones())
        	    .orElse(Collections.emptyList())
        	    .stream()
        	    .map(p -> new PhoneEntity(null, p.getNumber(), p.getCitycode(), p.getContrycode(), user))
        	    .toList();

        user.setPhones(phones);
        return user;
    }
}