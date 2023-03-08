package com.spring.practice.dmaker.controller;

import com.spring.practice.dmaker.dto.CreateDeveloper;
import com.spring.practice.dmaker.dto.DeveloperDTO;
import com.spring.practice.dmaker.dto.DeveloperDetailDTO;
import com.spring.practice.dmaker.dto.EditDeveloper;
import com.spring.practice.dmaker.service.DMakerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDTO> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDTO getDeveloperDetail(
            @PathVariable String memberId
    ) {
        log.info("GET /developer/{} HTTP/1.1", memberId);

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/developer")
    public CreateDeveloper.Response createOneDeveloper(
            @Valid @RequestBody CreateDeveloper.Request request
    ) {
        log.info("request: {}", request);
        return  dMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDTO editDeveloper(
            @PathVariable String memberId,
            @Valid @RequestBody EditDeveloper.Request request
    ) {
        log.info("PUT /developer/{} HTTP/1.1", memberId);

        return dMakerService.editDeveloper(memberId, request);
    }
}
