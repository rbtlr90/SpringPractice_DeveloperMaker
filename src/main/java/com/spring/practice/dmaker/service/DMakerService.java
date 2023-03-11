package com.spring.practice.dmaker.service;

import com.spring.practice.dmaker.code.StatusCode;
import com.spring.practice.dmaker.dto.CreateDeveloper;
import com.spring.practice.dmaker.dto.DeveloperDTO;
import com.spring.practice.dmaker.dto.DeveloperDetailDTO;
import com.spring.practice.dmaker.dto.EditDeveloper;
import com.spring.practice.dmaker.entity.Developer;
import com.spring.practice.dmaker.entity.RetiredDeveloper;
import com.spring.practice.dmaker.exception.DMakerException;
import com.spring.practice.dmaker.repository.DeveloperRepository;
import com.spring.practice.dmaker.repository.RetiredDeveloperRepository;
import com.spring.practice.dmaker.type.DeveloperLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.spring.practice.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCES_YEARS;
import static com.spring.practice.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCES_YEARS;
import static com.spring.practice.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
    private final DeveloperRepository developerRepository;
    private final RetiredDeveloperRepository retiredDeveloperRepository;

    @Transactional
    public CreateDeveloper.Response createDeveloper(
            CreateDeveloper.Request request
    ) {
        validateCreateDeveloperRequest(request);


        return CreateDeveloper.Response.fromEntity(
                developerRepository.save(
                        CreateDeveloper.Request.toEntity(request))
        );
    }

    private void validateCreateDeveloperRequest(
            @NonNull CreateDeveloper.Request request
    ) {
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );

        developerRepository.findByMemberId(request.getMemberId())
                .ifPresent((developer -> {
                    throw new DMakerException(DUPLICATED_MEMBER_ID);
                }));
    }

    @Transactional(readOnly = true)
    public List<DeveloperDTO> getAllActiveDevelopers() {
        return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
                .stream().map(DeveloperDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private Developer getDeveloperByMemberId(String memberId) {
        return developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    }

    @Transactional(readOnly = true)
    public DeveloperDetailDTO getDeveloperDetail(
            String memberId
    ) {
        return DeveloperDetailDTO.fromEntity(getDeveloperByMemberId(memberId));
    }

    @Transactional
    public DeveloperDetailDTO editDeveloper(
            String memberId, EditDeveloper.Request request
    ) {
        validateEditDeveloperRequest(memberId, request);

        return DeveloperDetailDTO.fromEntity(
                getUpdatedDeveloperFromRequest(
                        request,
                        getDeveloperByMemberId(memberId))
        );
    }

    private Developer getUpdatedDeveloperFromRequest(EditDeveloper.Request request, Developer developer) {
        developer.setDeveloperLevel(request.getDeveloperLevel());
        developer.setDeveloperSkillType(request.getDeveloperSkillType());
        developer.setExperienceYears(request.getExperienceYears());

        return developer;
    }

    private void validateEditDeveloperRequest(
            @NonNull String memberId,
            @NonNull EditDeveloper.Request request
    ) {
        developerRepository.findByMemberId(memberId).orElseThrow(
                () -> new DMakerException((NO_DEVELOPER))
        );
        validateDeveloperLevel(
                request.getDeveloperLevel(),
                request.getExperienceYears()
        );
    }

    private static void validateDeveloperLevel(
            @NonNull DeveloperLevel developerLevel,
            @NonNull Integer experienceYears
    ) {
        if (experienceYears < developerLevel.getMinExperienceYears()
                || experienceYears > developerLevel.getMaxExperienceYears()) {
            throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
        }
    }

    @Transactional
    public DeveloperDetailDTO deleteDeveloper(String memberId) {
        Developer developer = developerRepository.findByMemberId(memberId)
                .orElseThrow(() -> new DMakerException((NO_DEVELOPER)));
        developer.setStatusCode(StatusCode.RETIRED);

        RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .build();

        retiredDeveloperRepository.save(retiredDeveloper);
        return DeveloperDetailDTO.fromEntity(developer);
    }
}
