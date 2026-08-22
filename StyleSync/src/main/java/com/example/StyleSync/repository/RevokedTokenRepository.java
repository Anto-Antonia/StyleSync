package com.example.StyleSync.repository;

import com.example.StyleSync.entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Integer> {

    boolean existsByToken(String token);
    void deleteAllByExpiresAtBefore(LocalDateTime expiresAt);
}
