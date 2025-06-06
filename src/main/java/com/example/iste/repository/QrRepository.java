package com.example.iste.repository;


import com.example.iste.entity.QR.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface QrRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findFirstByUsernameAndExpiresAtAfterOrderByCreatedAtDesc(
            String username,
            LocalDateTime now
    );

    Optional<QrCode> findFirstByExpiresAtAfterOrderByCreatedAtDesc(LocalDateTime now);

}
