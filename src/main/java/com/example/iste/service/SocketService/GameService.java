package com.example.iste.service.SocketService;


import com.example.iste.entity.socket.game.DrawMessage;
import com.example.iste.entity.socket.game.GuessMessage;
import com.example.iste.repository.DrawMessageRepository;
import com.example.iste.repository.GuessMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {

    private final SimpMessagingTemplate messagingTemplate;
    private final List<String> users = new ArrayList<>();
    private int currentUserIndex = 0;
    private String currentWord = "";

    @Autowired
    private DrawMessageRepository drawMessageRepository;

    @Autowired
    private GuessMessageRepository guessMessageRepository;

    public GameService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    public void broadcastDraw(DrawMessage drawMessage) {
        drawMessageRepository.save(drawMessage);
        messagingTemplate.convertAndSend("/topic/draw", drawMessage);
    }

    public void processGuess(GuessMessage guessMessage) {
        guessMessageRepository.save(guessMessage);

        if (guessMessage.getGuess().equalsIgnoreCase(currentWord)) {
            messagingTemplate.convertAndSend("/topic/guess", Map.of(
                    "username", guessMessage.getUsername(),
                    "message", "Doğru tahmin! Kelime: " + currentWord
            ));
            scheduledNextTurn();
        } else {
            messagingTemplate.convertAndSend("/topic/guess", Map.of(
                    "username", guessMessage.getUsername(),
                    "message", guessMessage.getGuess()
            ));
        }
    }

    public void handleJoin(DrawMessage joinMessage) {
        String username = joinMessage.getUsername();
        if (!users.contains(username)) {
            users.add(username);
        }
        if(users.size() == 1) {
            startGame();
        }
        messagingTemplate.convertAndSend("/topic/users", users);
    }

    private void startGame() {
        currentUserIndex = -1;
        nextUser();
    }

    private void nextUser() {
        currentUserIndex = (currentUserIndex + 1) % users.size();
        System.out.println("Next drawer index: " + currentUserIndex + ", user: " + users.get(currentUserIndex));
        String drawer = users.get(currentUserIndex);
        currentWord = getRandomWord();
        messagingTemplate.convertAndSend("/topic/currentDrawer", Map.of(
                "currentDrawer", drawer
        ));
        messagingTemplate.convertAndSend("/topic/word", Map.of("word", currentWord));


    }


    public String getWordForUser(String username) {
        int index = Math.abs(username.hashCode()) % words.length;
        return words[index];
    }

    private final String[] words = {
            "masa", "sandalye", "telefon", "bilgisayar", "kitap", "defter",
            "kalem", "çanta", "ayna", "saat", "bardak", "tabak", "kaşık",
            "çatal", "bıçak", "televizyon", "yatak", "yorgan", "kanepe",
            "halı", "lamba", "klavye", "mouse", "priz", "şarj aleti", "dolap",
            "ayna", "perde", "cüzdan", "kutu"
    };

    private String getRandomWord() {
        return words[new Random().nextInt(words.length)];
    }
    public void setCurrentDrawer(String currentDrawer) {
        this.currentUserIndex = users.indexOf(currentDrawer);
        this.currentWord = getRandomWord();

        messagingTemplate.convertAndSend("/topic/currentDrawer", Map.of(
                "currentDrawer", currentDrawer
        ));
        messagingTemplate.convertAndSendToUser(currentDrawer, "/topic/word", Map.of("word", currentWord));

    }
    @Scheduled(fixedRate = 30000)
    public void scheduledNextTurn() {
        if (users.size() > 1) {
            nextUser();
        }
    }
}
