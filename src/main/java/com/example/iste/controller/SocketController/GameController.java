package com.example.iste.controller.SocketController;

import com.example.iste.entity.socket.game.DrawMessage;
import com.example.iste.entity.socket.game.GuessMessage;
import com.example.iste.service.SocketService.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class GameController {

    @Autowired
    private GameService gameService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/draw")
    public void handleDraw(DrawMessage drawMessage) {
        gameService.broadcastDraw(drawMessage);
    }

    @MessageMapping("/guess")
    public void handleGuess(GuessMessage guessMessage) {
        System.out.println("📨 Tahmin alındı: " + guessMessage.getUsername() + " -> " + guessMessage.getGuess());
        gameService.processGuess(guessMessage);
    }

    @MessageMapping("/join")
    public void handleJoin(DrawMessage joinMessage) {
        gameService.handleJoin(joinMessage);
    }
    @MessageMapping("/current-drawer")
    public void handleCurrentDrawer(Map<String, String> message) {
        String currentDrawer = message.get("currentDrawer");
        gameService.setCurrentDrawer(currentDrawer);
    }
    public void sendWordToUser(String username, String word) {
        simpMessagingTemplate.convertAndSendToUser(username, "/topic/word", Map.of("word", word));
    }



}
