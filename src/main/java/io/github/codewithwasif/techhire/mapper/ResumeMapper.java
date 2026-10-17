package io.github.codewithwasif.techhire.mapper;

import io.github.codewithwasif.techhire.dto.ResumeDto;
import io.github.codewithwasif.techhire.entity.ResumeEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ResumeMapper {

    public ResumeEntity toEntity(ResumeDto.CreateResumeRequest createRequest, UserEntity candidate){
        if (createRequest == null) return null;
        return ResumeEntity.builder()
                .fullName(createRequest.fullName())
                .professionalTitle(createRequest.professionalTitle())
                .skills(createRequest.skills())
                .portfolioUrl(createRequest.portfolioUrl())
                .bio(createRequest.bio())
                .candidateDetails(candidate)
                .build();
    }

    public ResumeDto.ResumeResponse toResponse(ResumeEntity resume){
        if (resume == null) return null;
        return ResumeDto.ResumeResponse.builder()
                .id(resume.getId())
                .fullName(resume.getFullName())
                .professionalTitle(resume.getProfessionalTitle())
                .skills(resume.getSkills())
                .portfolioUrl(resume.getPortfolioUrl())
                .bio(resume.getBio())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }

    public List<ResumeDto.ResumeResponse> toResponseList(List<ResumeEntity> resumeList){
        if (resumeList == null || resumeList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ResumeDto.ResumeResponse> responseList = new ArrayList<>();

        for (ResumeEntity resume : resumeList) {
            responseList.add(toResponse(resume));
        }
        return responseList;
    }

}
