package io.github.codewithwasif.techhire.mapper;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.dto.ResumeDto;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.ResumeEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JobPostMapper {

    public JobPostEntity toEntity(JobPostDto.CreateJobPostRequest createRequest, UserEntity employer) {
        if (createRequest == null) return null;

        return JobPostEntity.builder().title(createRequest.title())
                .companyName(createRequest.companyName())
                .description(createRequest.description())
                .minSalary(createRequest.minSalary())
                .maxSalary(createRequest.maxSalary())
                .techStack(createRequest.techStack())
                .status(createRequest.status())
                .employerDetails(employer)
                .build();
    }

    public JobPostDto.JobPostResponse toResponse(JobPostEntity jobPostEntity) {
        if (jobPostEntity == null) return null;

        return JobPostDto.JobPostResponse.builder()
                .id(jobPostEntity.getId())
                .title(jobPostEntity.getTitle())
                .companyName(jobPostEntity.getCompanyName())
                .description(jobPostEntity.getDescription())
                .minSalary(jobPostEntity.getMinSalary())
                .maxSalary(jobPostEntity.getMaxSalary())
                .techStack(jobPostEntity.getTechStack())
                .status(jobPostEntity.getStatus())
                .createdAt(jobPostEntity.getCreatedAt())
                .updatedAt(jobPostEntity.getUpdatedAt())
                .build();
    }

    public List<JobPostDto.JobPostResponse> toGetAllPosts(List<JobPostEntity> allJobsById) {
        if (allJobsById == null || allJobsById.isEmpty()) {
            return Collections.emptyList();
        }

        List<JobPostDto.JobPostResponse> jobPostDto = new ArrayList<>();

        for (JobPostEntity jobPostEntity : allJobsById) {
            if (jobPostEntity == null) continue;
            jobPostDto.add(toResponse(jobPostEntity));
        }
        return jobPostDto;
    }

    public void updateEntityFromDto(JobPostDto.UpdateJobPostRequest request, JobPostEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        if (StringUtils.hasText(request.title())) {
            entity.setTitle(request.title());
        }
        if (StringUtils.hasText(request.companyName())) {
            entity.setCompanyName(request.companyName());
        }
        if (StringUtils.hasText(request.description())) {
            entity.setDescription(request.description());
        }
        if (request.minSalary() != null && request.minSalary() >= 0) {
            entity.setMinSalary(request.minSalary());
        }
        if (request.maxSalary() != null && request.maxSalary() >= 0) {
            entity.setMaxSalary(request.maxSalary());
        }
        if (request.techStack() != null && !request.techStack().isEmpty()) {
            entity.setTechStack(request.techStack());
        }
        if (StringUtils.hasText(request.status())) {
            entity.setStatus(request.status());
        }
    }


    public List<JobPostDto.ApplicantReviewResponse> toApplicantReviewResponse(List<JobApplyEntity> applicants, Map<Long, ResumeEntity> resumes) {
        if (applicants == null || applicants.isEmpty()) {
            return Collections.emptyList();
        }

        List<JobPostDto.ApplicantReviewResponse> applicantReviewResponses = new ArrayList<>();

        for (JobApplyEntity applyDto : applicants) {
            if (applyDto == null) continue;

            ResumeEntity resumeEntity = resumes != null ? resumes.get(applyDto.getResumeId()) : null;

            ResumeDto.EmployerResumeResponse resumeDto = resumeEntity != null ? ResumeDto.EmployerResumeResponse.builder()
                    .fullName(resumeEntity.getFullName())
                    .professionalTitle(resumeEntity.getProfessionalTitle())
                    .skills(resumeEntity.getSkills())
                    .portfolioUrl(resumeEntity.getPortfolioUrl())
                    .bio(resumeEntity.getBio())
                    .build() : null;

            JobApplyDto.EmployerResponse applicantsDto = JobApplyDto.EmployerResponse.builder()
                    .coverLetterMessage(applyDto.getCoverLetterMessage())
                    .build();

            JobPostDto.ApplicantReviewResponse response = new JobPostDto.ApplicantReviewResponse(applicantsDto, resumeDto);
            applicantReviewResponses.add(response);
        }
        return applicantReviewResponses;
    }

    public List<JobPostDto.ShowAllPostsResponse> toShowAllPostsResponse(List<JobPostEntity> employerEntities) {
        if (employerEntities == null || employerEntities.isEmpty()) {
            return Collections.emptyList();
        }

        List<JobPostDto.ShowAllPostsResponse> result = new ArrayList<>();

        for (JobPostEntity all : employerEntities) {
            if (all == null) continue;

            JobPostDto.ShowAllPostsResponse build = JobPostDto.ShowAllPostsResponse.builder()
                    .id(all.getId())
                    .title(all.getTitle())
                    .companyName(all.getCompanyName())
                    .description(all.getDescription())
                    .minSalary(all.getMinSalary())
                    .maxSalary(all.getMaxSalary())
                    .techStack(all.getTechStack())
                    .status(all.getStatus())
                    .createdAt(all.getCreatedAt())
                    .build();

            result.add(build);
        }
        return result;
    }
}
