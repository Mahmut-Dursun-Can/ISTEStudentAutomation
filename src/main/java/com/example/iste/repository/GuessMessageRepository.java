package com.example.iste.repository;


import com.example.iste.entity.socket.game.GuessMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuessMessageRepository extends JpaRepository<GuessMessage, Long> {
}
