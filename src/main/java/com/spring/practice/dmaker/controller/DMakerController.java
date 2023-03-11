package com.spring.practice.dmaker.controller;

import com.spring.practice.dmaker.dto.*;
import com.spring.practice.dmaker.exception.DMakerException;
import com.spring.practice.dmaker.service.DMakerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DMakerController {
    private final DMakerService dMakerService;

    @GetMapping("/developers")
    public List<DeveloperDTO> getAllDevelopers() {
        log.info("GET /developers HTTP/1.1");

        return dMakerService.getAllActiveDevelopers();
    }

    @GetMapping("/developer/{memberId}")
    public DeveloperDetailDTO getDeveloperDetail(
            @PathVariable final String memberId
    ) {
        log.info("GET /developer/{} HTTP/1.1", memberId);

        return dMakerService.getDeveloperDetail(memberId);
    }

    @PostMapping("/developer")
    public CreateDeveloper.Response createOneDeveloper(
            @Valid @RequestBody final CreateDeveloper.Request request
    ) {
        log.info("request: {}", request);
        return  dMakerService.createDeveloper(request);
    }

    @PutMapping("/developer/{memberId}")
    public DeveloperDetailDTO editDeveloper(
            @PathVariable final String memberId,
            @Valid @RequestBody final EditDeveloper.Request request
    ) {
        log.info("PUT /developer/{} HTTP/1.1", memberId);

        return dMakerService.editDeveloper(memberId, request);
    }

    @DeleteMapping("developer/{memberId}")
    public DeveloperDetailDTO deleteDeveloper(
            @PathVariable final String memberId
    ) {
        return dMakerService.deleteDeveloper(memberId);
    }
}
