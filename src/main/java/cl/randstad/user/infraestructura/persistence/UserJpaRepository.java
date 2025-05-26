package cl.randstad.user.infraestructura.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.randstad.user.infraestructura.persistence.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
	boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);
}
