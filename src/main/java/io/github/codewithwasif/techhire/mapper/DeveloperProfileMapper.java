package io.github.codewithwasif.techhire.mapper;

import io.github.codewithwasif.techhire.dto.DeveloperProfileDto;
import io.github.codewithwasif.techhire.entity.DeveloperProfileEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeveloperProfileMapper {

    public DeveloperProfileEntity toEntity(
            DeveloperProfileDto.CreateDeveloperProfileRequest request,
            UserEntity developer,
            float[] embedding
    ) {
        if (request == null) {
            return null;
        }

        return DeveloperProfileEntity.builder()
                .fullName(request.fullName())
                .headline(request.headline())
                .skills(request.skills())
                .workPreferences(request.workPreferences())
                .yearsOfExperience(request.yearsOfExperience())
                .summary(request.summary())
                .location(request.location())
                .developer(developer)
                .embedding(embedding)
                .build();
    }

    public DeveloperProfileDto.DeveloperProfileResponse toResponse(DeveloperProfileEntity profile) {
        if (profile == null) {
            return null;
        }

        return DeveloperProfileDto.DeveloperProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .headline(profile.getHeadline())
                .skills(profile.getSkills())
                .workPreferences(profile.getWorkPreferences())
                .yearsOfExperience(profile.getYearsOfExperience())
                .summary(profile.getSummary())
                .location(profile.getLocation())
                .createdAt(profile.getCreatedAt())
                .build();
    }

    public String buildEmbeddingText(DeveloperProfileDto.CreateDeveloperProfileRequest profile) {
        if (profile == null) {
            return "";
        }

        return """
            Headline: %s
            Years of Experience: %.1f
            Skills: %s
            Work Preference: %s
            Location: %s
            Summary: %s
            """.formatted(
                defaultIfBlank(profile.headline()),
                profile.yearsOfExperience(),
                formatSkills(profile.skills()),
                profile.workPreferences() != null ? profile.workPreferences().name() : "None",
                defaultIfBlank(profile.location()),
                defaultIfBlank(profile.summary())
        ).trim();
    }

    private String formatSkills(List<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return "None";
        }
        return String.join(", ", skills);
    }

    private String defaultIfBlank(String value) {
        return (value != null && !value.isBlank()) ? value.trim() : "None";
    }
}

