package com.example.iste.controller.SocketController;

import com.example.iste.entity.socket.ChatMessage;
import com.example.iste.service.SocketService.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageService chatMessageService; // Mesajları veritabanına kaydedecek servis


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        System.out.println("Mesaj alındı: " + chatMessage.getContent());

        chatMessageService.saveChatMessage(chatMessage);


        return chatMessage;
    }
}
