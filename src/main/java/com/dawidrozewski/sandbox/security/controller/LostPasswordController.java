package com.dawidrozewski.sandbox.security.controller;


import com.dawidrozewski.sandbox.security.model.dto.ChangePassword;
import com.dawidrozewski.sandbox.security.model.dto.EmailObject;
import com.dawidrozewski.sandbox.security.service.LostPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LostPasswordController {

    private final LostPasswordService lostPasswordService;

    @PostMapping("/lostPassword")
    public void lostPassword(@RequestBody EmailObject emailObject) {
        lostPasswordService.sendLostPasswordLink(emailObject);

    }

    @PostMapping("/changePassword")
    public void changePassword(@RequestBody ChangePassword changePassword) {
        lostPasswordService.changePassword(changePassword);
    }
}
