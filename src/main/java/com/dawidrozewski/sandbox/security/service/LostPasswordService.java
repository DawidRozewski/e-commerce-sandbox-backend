package com.dawidrozewski.sandbox.security.service;

import com.dawidrozewski.sandbox.common.mail.EmailClientService;
import com.dawidrozewski.sandbox.security.model.User;
import com.dawidrozewski.sandbox.security.model.dto.ChangePassword;
import com.dawidrozewski.sandbox.security.model.dto.EmailObject;
import com.dawidrozewski.sandbox.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class LostPasswordService {

    private final UserRepository userRepository;
    private final EmailClientService emailClientService;
    @Value("${app.serviceAddress}")
    private String serviceAddress;


    @Transactional
    public void sendLostPasswordLink(EmailObject emailObject) {
        User user = userRepository.findByUsername(emailObject.getEmail()).orElseThrow(() -> new RuntimeException("Email do not exist."));
        String hash = generateHashForLostPassword(user);
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now());

        emailClientService.getInstance()
                .send(emailObject.getEmail(), "Reset your password", createMessage(createLink(hash)));

    }

    private String createMessage(String hashLink) {
        return "We have generated a password reset link for you" +
                "\n\n Click the link to reset your password: " +
                "\n" + hashLink +
                "\n\n Thank you.";
    }

    private String createLink(String hash) {
        return serviceAddress + "/lostPassword/" + hash;
    }

    private String generateHashForLostPassword(User user) {
        String toHash = user.getId() + user.getUsername() + user.getPassword() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    @Transactional
    public void changePassword(ChangePassword changePassword) {
        if (!Objects.equals(changePassword.getPassword(), changePassword.getRepeatPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }
        User user = userRepository.findByHash(changePassword.getHash())
                .orElseThrow(() -> new RuntimeException("Invalid link"));
        if (user.getHashDate().plusMinutes(10).isAfter(LocalDateTime.now())) {
            user.setPassword("{bcrypt}" + new BCryptPasswordEncoder().encode(changePassword.getPassword()));
            user.setHash(null);
            user.setHashDate(null);
        } else {
            throw new RuntimeException("Link expired.");
        }
    }
}
