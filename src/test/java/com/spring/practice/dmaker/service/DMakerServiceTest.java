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

import static com.spring.practice.dmaker.type.DeveloperLevel.*;
import static com.spring.practice.dmaker.type.DeveloperSkillType.*;
import static org.junit.jupiter.api.Assertions.*;
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
//        CreateDeveloper.Request request = CreateDeveloper.Request.builder()
//                .developerLevel(SENIOR)
//                .developerSkillType(BACK_END)
//                .experienceYears(10)
//                .memberId("memberId")
//                .name("test1")
//                .age(34)
//                .build();

        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());

        ArgumentCaptor<Developer> captor =
                ArgumentCaptor.forClass(Developer.class);

        // when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(defaultCreateRequest);

        // then
        verify(developerRepository, times(1))
                .save(captor.capture());

        Developer savedDeveloper = captor.getValue();
        assertEquals(SENIOR, savedDeveloper.getDeveloperLevel());
        assertEquals(BACK_END, savedDeveloper.getDeveloperLevel());
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

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());

    }

}