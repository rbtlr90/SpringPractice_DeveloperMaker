package com.spring.practice.dmaker.controller;

import com.spring.practice.dmaker.dto.CreateDeveloper;
import com.spring.practice.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<String> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return Arrays.asList("tester1", "tester2", "tester3");
    }

    @PostMapping("/developer")
    public CreateDeveloper.Response createOneDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
    ) {
        log.info("request: {}", request);
        return  dMakerService.createDeveloper(request);
    }
}
