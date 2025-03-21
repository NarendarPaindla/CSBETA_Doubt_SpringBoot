package com.example.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class PasswordRestService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String FROM_EMAIL = "";// Sender's email address
    private final String RESET_URL = "http://localhost:5173/reset-password";
  
    public boolean generateResetToken(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String resetLink = RESET_URL + "?token=" + token;
        sendResetEmail(email, resetLink);
        return true;
    }
    public boolean resetPassword(String token, String newPassword) {
        Optional<User> optionalUser = userRepository.findAll().stream()
                .filter(u -> token.equals(u.getResetToken()) &&
                        u.getResetTokenExpiry() != null &&
                        u.getResetTokenExpiry().isAfter(LocalDateTime.now()))
                .findFirst();
        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
        return true;
    }

    private void sendResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM_EMAIL);
        message.setTo(toEmail);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the following link:\n"
                + resetLink + "\n\nThis link is valid for 1 hour.");
        mailSender.send(message);
    }
}
