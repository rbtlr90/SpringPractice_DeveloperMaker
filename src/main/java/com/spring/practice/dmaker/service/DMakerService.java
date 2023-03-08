package com.spring.practice.dmaker.service;

import com.spring.practice.dmaker.dto.CreateDeveloper;
import com.spring.practice.dmaker.dto.DeveloperDTO;
import com.spring.practice.dmaker.dto.DeveloperDetailDTO;
import com.spring.practice.dmaker.dto.EditDeveloper;
import com.spring.practice.dmaker.entity.Developer;
import com.spring.practice.dmaker.exception.DMakerException;
import com.spring.practice.dmaker.repository.DeveloperRepository;
import com.spring.practice.dmaker.type.DeveloperLevel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.spring.practice.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(CreateDeveloper.Request request) {
        validateCreateDeveloperRequest(request);

        Developer developer = Developer.builder()
                .developerLevel(request.getDeveloperLevel())
                .developerSkillType(request.getDeveloperSkillType())
                .experienceYears(request.getExperienceYears())
                .memberId(request.getMemberId())
                .name(request.getName())
                .age(request.getAge())
                .build();
        developerRepository.save(developer);
        return CreateDeveloper.Response.fromEntity(developer);
    }

    private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    public List<DeveloperDTO> getAllDevelopers() {
        return developerRepository.findAll()
                .stream().map(DeveloperDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public DeveloperDetailDTO getDeveloperDetail(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .map(DeveloperDetailDTO::fromEntity)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    @Transactional
    public DeveloperDetailDTO editDeveloper(String memberId, EditDeveloper.Request request) {
        validateEditDeveloperRequest(memberId, request);

        Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException(NO_DEVELOPER)
        );

        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return DeveloperDetailDTO.fromEntity(developer);
    }

    private void validateEditDeveloperRequest(
            String memberId,
            EditDeveloper.Request request
    ) {
        developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException((NO_DEVELOPER))
        );
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );
    }

    private static void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
        if (developerLevel == DeveloperLevel.SENIOR
                && experienceYears < 10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNGNIOR
                && experienceYears < 4 || experienceYears > 10) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
        if (developerLevel == DeveloperLevel.JUNIOR
                && experienceYears > 4) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }
}
