package com.example.iste.repository;

import com.example.iste.entity.socket.game.DrawMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawMessageRepository extends JpaRepository<DrawMessage, Long> {
}
