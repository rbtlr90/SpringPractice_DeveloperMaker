package com.spring.practice.dmaker.dto;

import com.spring.practice.dmaker.entity.Developer;
import com.spring.practice.dmaker.type.DeveloperLevel;
import com.spring.practice.dmaker.type.DeveloperSkillType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeveloperDetailDTO {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private Integer experienceYears;
    private String memberId;
    private String name;
    private Integer age;

    public static DeveloperDetailDTO fromEntity(Developer developer){
        return DeveloperDetailDTO.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .experienceYears(developer.getExperienceYears())
                .memberId(developer.getMemberId())
                .name(developer.getName())
                .age(developer.getAge())
                .build();
    }
}
