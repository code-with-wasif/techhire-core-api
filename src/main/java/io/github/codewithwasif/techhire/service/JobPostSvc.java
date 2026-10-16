package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.dto.ResumeDto;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.ResumeEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import org.springframework.security.access.AccessDeniedException;
import io.github.codewithwasif.techhire.exception.BadRequestException;
import io.github.codewithwasif.techhire.exception.ResourceNotFoundException;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import io.github.codewithwasif.techhire.repository.ResumeRepo;
import io.github.codewithwasif.techhire.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobPostSvc {

    private final JobPostRepo jobPostRepo;
    private final ResumeRepo resumeRepo;
    private final SecurityUtils securityUtils;

    @Transactional
    public JobPostDto.CreateJobPostRequest createJob(JobPostDto.CreateJobPostRequest createRequest){
        if (createRequest.minSalary() > createRequest.maxSalary()) {
            throw new BadRequestException("Minimum salary cannot exceed maximum salary.");
        }
        UserEntity employer = securityUtils.getCurrentUser();

        JobPostEntity job = JobPostEntity.builder().title(createRequest.title())
                    .companyName(createRequest.companyName())
                    .description(createRequest.description())
                    .minSalary(createRequest.minSalary())
                    .maxSalary(createRequest.maxSalary())
                    .techStack(createRequest.techStack())
                    .status(createRequest.status())
                    .employerDetails(employer)
                    .build();
            jobPostRepo.save(job);
            return createRequest;
    }

    public List<JobPostDto.JobPostResponse> getMyJobs(){
        UserEntity user = securityUtils.getCurrentUser();
        Long userId = user.getId();

        List<JobPostEntity> allJobsById = jobPostRepo.getAllJobsById(userId);

        List<JobPostDto.JobPostResponse> jobPostDto = new ArrayList<>();
        for(JobPostEntity jobPostEntity:allJobsById ) {
            jobPostDto.add(JobPostDto.JobPostResponse.builder()
                    .id(jobPostEntity.getId())
                    .title(jobPostEntity.getTitle())
                    .companyName(jobPostEntity.getCompanyName())
                    .description(jobPostEntity.getDescription())
                    .minSalary(jobPostEntity.getMinSalary())
                    .maxSalary(jobPostEntity.getMaxSalary())
                    .techStack(jobPostEntity.getTechStack())
                    .status(jobPostEntity.getStatus())
                    .build()) ;
        }
        return jobPostDto;
    }

    public JobPostDto.UpdateJobPostRequest changePostEntry(JobPostDto.UpdateJobPostRequest newEntry, Long id){
        JobPostEntity oldEntry = fetchJobPostById(id);
        UserEntity employer = securityUtils.getCurrentUser();
        validateOwnership(oldEntry, employer);

        int updatedMin = newEntry.minSalary() != null ? newEntry.minSalary() : oldEntry.getMinSalary();
        int updatedMax = newEntry.maxSalary() != null ? newEntry.maxSalary() : oldEntry.getMaxSalary();
        if (updatedMin > updatedMax) {
            throw new BadRequestException("Minimum salary cannot exceed maximum salary.");
        }
                if (StringUtils.hasText(newEntry.title())) oldEntry.setTitle(newEntry.title());
                if (StringUtils.hasText(newEntry.companyName())) oldEntry.setCompanyName(newEntry.companyName());
                if (StringUtils.hasText(newEntry.description())) oldEntry.setDescription(newEntry.description());
                if (newEntry.minSalary() != null && newEntry.minSalary() >= 0)
                    oldEntry.setMinSalary(newEntry.minSalary());
                if (newEntry.maxSalary() != null && newEntry.maxSalary() >= 0)
                    oldEntry.setMaxSalary(newEntry.maxSalary());
                if (newEntry.techStack() != null && !newEntry.techStack().isEmpty())
                    oldEntry.setTechStack(newEntry.techStack());
                if (StringUtils.hasText(newEntry.status())) oldEntry.setStatus(newEntry.status());

                jobPostRepo.save(oldEntry);
                return newEntry;
        }

    public void deleteJobPost(Long id) {

        UserEntity employer = securityUtils.getCurrentUser();
        JobPostEntity jobToDelete = fetchJobPostById(id);
        validateOwnership(jobToDelete, employer);
        jobPostRepo.deleteById(id);
    }

    public List<JobPostDto.ApplicantReviewResponse> getApplicantsById(Long id){
        JobPostEntity jobOfApplicants = fetchJobPostById(id);
        UserEntity employer = securityUtils.getCurrentUser();

        validateOwnership(jobOfApplicants, employer);

                List<JobApplyEntity> applicants = jobOfApplicants.getApplications();
                List<JobPostDto.ApplicantReviewResponse> applicantReviewResponses = new ArrayList<>();

                for (JobApplyEntity applyDto : applicants) {

                    Long resumeId = applyDto.getResumeId();
                    ResumeEntity resumeEntity = resumeRepo.findById(resumeId).orElseThrow(()->{ log.error("Resume not found with Id {}", resumeId);
                        return new ResourceNotFoundException("Resume not found with Id "+ resumeId);});

                    ResumeDto.EmployerResumeResponse resumeDto = ResumeDto.EmployerResumeResponse.builder()
                            .fullName(resumeEntity.getFullName())
                            .professionalTitle(resumeEntity.getProfessionalTitle())
                            .skills(resumeEntity.getSkills())
                            .portfolioUrl(resumeEntity.getPortfolioUrl())
                            .bio(resumeEntity.getBio())
                            .build();

                    JobApplyDto.EmployerResponse applicantsDto = JobApplyDto.EmployerResponse.builder()
                            .coverLetterMessage(applyDto.getCoverLetterMessage())
                            .build();

                    JobPostDto.ApplicantReviewResponse response = new JobPostDto.ApplicantReviewResponse(applicantsDto, resumeDto);
                    applicantReviewResponses.add(response);
                }
                return applicantReviewResponses;
            }


    private JobPostEntity fetchJobPostById(Long id) {
        return jobPostRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found with ID: " + id));
    }

    private void validateOwnership(JobPostEntity job, UserEntity employer) {
        if (job.getEmployerDetails() == null || !job.getEmployerDetails().getId().equals(employer.getId())) {
            throw new AccessDeniedException("This job post does not belong to you.");
        }
    }
    }