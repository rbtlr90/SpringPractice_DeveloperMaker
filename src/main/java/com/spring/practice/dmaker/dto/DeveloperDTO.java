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
public class DeveloperDTO {
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private String memberId;

    public static DeveloperDTO fromEntity(Developer developer) {
        return DeveloperDTO.builder()
                .developerLevel(developer.getDeveloperLevel())
                .developerSkillType(developer.getDeveloperSkillType())
                .memberId(developer.getMemberId())
                .build();
    }
}
