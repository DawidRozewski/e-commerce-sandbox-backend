package com.dawidrozewski.sandbox.homepage.controller;

import com.dawidrozewski.sandbox.homepage.controller.dto.HomepageDto;
import com.dawidrozewski.sandbox.homepage.service.HomepageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomepageController {

    private final HomepageService homepageService;

    @GetMapping("/homepage")
    public HomepageDto getHomepage() {
       return new HomepageDto(homepageService.getSaleProducts());
    }
}
