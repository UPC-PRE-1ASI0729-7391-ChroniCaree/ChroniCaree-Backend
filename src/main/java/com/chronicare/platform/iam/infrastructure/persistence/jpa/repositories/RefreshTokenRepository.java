package com.chronicare.platform.iam.infrastructure.persistence.jpa.repositories;

import com.chronicare.platform.iam.domain.model.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("delete from refresh_tokens rt where rt.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
