package com.spring.practice.dmaker.service;

import com.spring.practice.dmaker.dto.CreateDeveloper;
import com.spring.practice.dmaker.entity.Developer;
import com.spring.practice.dmaker.exception.DMakerErrorCode;
import com.spring.practice.dmaker.exception.DMakerException;
import com.spring.practice.dmaker.repository.DeveloperRepository;
import com.spring.practice.dmaker.repository.RetiredDeveloperRepository;
import com.spring.practice.dmaker.type.DeveloperLevel;
import com.spring.practice.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.spring.practice.dmaker.constant.DMakerConstant.MAX_JUNIOR_EXPERIENCES_YEARS;
import static com.spring.practice.dmaker.constant.DMakerConstant.MIN_SENIOR_EXPERIENCES_YEARS;
import static com.spring.practice.dmaker.exception.DMakerErrorCode.DUPLICATED_MEMBER_ID;
import static com.spring.practice.dmaker.exception.DMakerErrorCode.LEVEL_EXPERIENCE_YEARS_NOT_MATCHED;
import static com.spring.practice.dmaker.type.DeveloperLevel.*;
import static com.spring.practice.dmaker.type.DeveloperSkillType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {
    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(SENIOR)
            .developerSkillType(BACK_END)
            .experienceYears(10)
            .memberId("memberId")
            .name("test1")
            .age(34)
            .build();

    private CreateDeveloper.Request getCreateRequest (
            DeveloperLevel developerLevel,
            DeveloperSkillType developerSkillType,
            Integer experienceYears
    ) {
        return CreateDeveloper.Request.builder()
                .developerLevel(developerLevel)
                .developerSkillType(developerSkillType)
                .experienceYears(experienceYears)
                .memberId("memberId")
                .name("test1")
                .age(34)
                .build();
    }

    private final CreateDeveloper.Request defaultCreateRequest = CreateDeveloper.Request.builder()
            .developerLevel(SENIOR)
            .developerSkillType(BACK_END)
            .experienceYears(10)
            .memberId("memberId")
            .name("test1")
            .age(34)
            .build();

    @Test
    void createDeveloperTest_success() {
        // given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        given(developerRepository.save(any()))
                .willReturn(defaultDeveloper);

        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        // when
        dMakerService.createDeveloper(defaultCreateRequest);

        // then
        verify(developerRepository, times(1))
                .save(captor.capture());

        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(BACK_END, savedDeveloper.getDeveloperSkillType());
        assertEquals(10, savedDeveloper.getExperienceYears());
        assertEquals("test1", savedDeveloper.getName());
    }

    @Test
    void createDeveloperTest_failed_with_conflict() {
        // given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        // when
        // then
        DMakerException dMakerException = assertThrows(
                DMakerException.class,
                () -> dMakerService.createDeveloper(defaultCreateRequest)
        );

        assertEquals(DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }

    @Test
    void createDeveloperTest_fail_with_unmatched_level() {
        // given
        // when
        // then
        DMakerException dMakerException = assertThrows(
                DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(SENIOR, FRONT_END, MIN_SENIOR_EXPERIENCES_YEARS - 2)
                )
        );

        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());

        dMakerException = assertThrows(
                DMakerException.class,
                () -> dMakerService.createDeveloper(
                        getCreateRequest(JUNIOR, FRONT_END, MAX_JUNIOR_EXPERIENCES_YEARS +  2)
                )
        );

        assertEquals(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED, dMakerException.getDMakerErrorCode());
    }

}