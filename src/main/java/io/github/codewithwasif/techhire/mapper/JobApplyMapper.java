package io.github.codewithwasif.techhire.mapper;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class JobApplyMapper {

    public JobApplyEntity toEntity(JobApplyDto.CreateApplicationRequest createApplicationRequest,
                                   JobPostEntity jobToApply,
                                   UserEntity applicant){
        return JobApplyEntity.builder()
                .coverLetterMessage(createApplicationRequest.coverLetterMessage())
                .applicationStatus("PENDING")
                .resumeId(createApplicationRequest.resumeId())
                .jobDetails(jobToApply)
                .applicantDetails(applicant)
                .build();
    }

    public JobApplyDto.ApplicationResponse toResponse(JobApplyEntity entity) {
        if (entity == null) return null;

        return JobApplyDto.ApplicationResponse.builder()
                .id(entity.getId())
                .coverLetterMessage(entity.getCoverLetterMessage())
                .applicationStatus(entity.getApplicationStatus())
                .resumeId(entity.getResumeId())
                .jobId(entity.getJobDetails() != null ? entity.getJobDetails().getId() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
