package com.example.iste.controller;

import com.example.iste.entity.Appointment;
import com.example.iste.entity.Comment;
import com.example.iste.service.AppointmentService;
import com.example.iste.service.CommentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@SessionAttributes("username")  // Oturumda username'i tutmak
public class CommentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/api/comments")
    public String submitComment(@ModelAttribute Comment comment, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");

        if (username == null) {
            redirectAttributes.addFlashAttribute("error", "Kullanıcı oturum bilgisi bulunamadı.");
            return "redirect:/login-user";
        }

        Comment existingComment = commentService.findByUsername(username);

        if (existingComment != null) {
            redirectAttributes.addFlashAttribute("error", "Zaten daha önceden yorum yaptınız.");
            return "redirect:/user-dashboard";
        }

        comment.setUsername(username);

        commentService.saveComment(comment.getContent(), comment.getStars(), comment.getUsername());

        redirectAttributes.addFlashAttribute("success", "Yorum başarıyla gönderildi!");

        return "redirect:/user-dashboard";
    }
}