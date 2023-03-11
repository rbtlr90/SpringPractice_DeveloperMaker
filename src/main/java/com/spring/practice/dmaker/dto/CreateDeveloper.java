package com.spring.practice.dmaker.dto;

import com.spring.practice.dmaker.code.StatusCode;
import com.spring.practice.dmaker.entity.Developer;
import com.spring.practice.dmaker.type.DeveloperLevel;
import com.spring.practice.dmaker.type.DeveloperSkillType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

public class CreateDeveloper {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Request {
        @NotNull
        private DeveloperLevel developerLevel;
        @NotNull
        private DeveloperSkillType developerSkillType;
        @NotNull
        @Min(0)
        @Max(20)
        private Integer experienceYears;
        @NotNull
        @Size(min = 3, max = 50, message = "memberId length must be in 3 ~ 50 characters")
        private String memberId;
        @NotNull
        @Size(min = 3, max = 50, message = "name length must be in 3 ~ 20 characters")
        private String name;
        @Min(18)
        private Integer age;

        public static Developer toEntity(CreateDeveloper.Request request) {
            return Developer.builder()
                    .developerLevel(request.getDeveloperLevel())
                    .developerSkillType(request.getDeveloperSkillType())
                    .experienceYears(request.getExperienceYears())
                    .memberId(request.getMemberId())
                    .name(request.getName())
                    .age(request.getAge())
                    .statusCode(StatusCode.EMPLOYED)
                    .build();
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class Response {
        private DeveloperLevel developerLevel;
        private DeveloperSkillType developerSkillType;
        private Integer experienceYears;
        private String memberId;

        public static Response fromEntity(@NonNull Developer developer) {
            return Response.builder()
                    .developerLevel(developer.getDeveloperLevel())
                    .developerSkillType(developer.getDeveloperSkillType())
                    .experienceYears(developer.getExperienceYears())
                    .memberId(developer.getMemberId())
                    .build();
        }
    }

}
