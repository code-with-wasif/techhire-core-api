package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.dto.ResumeDto;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.ResumeEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import io.github.codewithwasif.techhire.repository.ResumeRepo;
import io.github.codewithwasif.techhire.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepo userRepo;
    private final ResumeRepo resumeRepo;

    @Transactional
    public ResponseEntity<HttpStatus> createJob(JobPostDto.CreateJobPostRequest createRequest){
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            String name = context.getAuthentication().getName();
            if (name == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            UserEntity employer = userRepo.findByUserName(name);

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
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while creating post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<JobPostDto.JobPostResponse>> getMyJobs(){
            SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity user = userRepo.findByUserName(name);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
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
        return new ResponseEntity<>(jobPostDto, HttpStatus.OK);
    }

    public ResponseEntity<HttpStatus> changePostEntry(JobPostDto.UpdateJobPostRequest newEntry, Long id){
        JobPostEntity oldEntry = jobPostRepo.findById(id).orElseThrow(() ->{ log.error("Job Post Not Found With Id: {}", id);
            return new NullPointerException();});
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity employer = userRepo.findByUserName(name);
        try {
            if (oldEntry.getEmployerDetails() != null && oldEntry.getEmployerDetails().getId().equals(employer.getId())) {
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
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                log.warn("Employer {} attempted to edit Job {} without ownership.", name, id);
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            log.error("Error updating post with ID: {}.", id, e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<HttpStatus> deleteJobPost(Long id){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        try {
            if (name != null) {
                UserEntity employer = userRepo.findByUserName(name);
                JobPostEntity jobToDelete = jobPostRepo.findById(id).orElse(null);
                if (jobToDelete == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                if (jobToDelete.getEmployerDetails() != null &&
                        jobToDelete.getEmployerDetails().getId().equals(employer.getId())) {
                    jobPostRepo.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                } else {
                    return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                }
            }
        }
         catch (Exception e) {
            log.error("No post found for User: {} with this ID: {}.", name, id, e);
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<List<JobPostDto.ApplicantReviewResponse>> getApplicantsById(Long id){
        JobPostEntity jobOfApplicants = jobPostRepo.findById(id).orElseThrow(() ->{ log.error("Job Post Not Found With Id {}", id);
            return new NullPointerException();});
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity employer = userRepo.findByUserName(name);
        try {
            if (jobOfApplicants.getEmployerDetails() != null && jobOfApplicants.getEmployerDetails().getId().equals(employer.getId())) {
                List<JobApplyEntity> applicants = jobOfApplicants.getApplications();
                List<JobPostDto.ApplicantReviewResponse> applicantReviewResponses = new ArrayList<>();

                for (JobApplyEntity applyDto : applicants) {
                    Long resumeId = applyDto.getResumeId();
                    ResumeEntity resumeEntity = resumeRepo.findById(resumeId).orElseThrow(()->{ log.error("Resume not found with Id {}", resumeId);
                        return new NullPointerException();});

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
                return new ResponseEntity<>(applicantReviewResponses, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("No post found for User: {} with this ID: {}.", name, id, e);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
