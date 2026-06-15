package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobPostSvc {

    private final JobPostRepo jobPostRepo;
    private final UserRepo userRepo;

    @Transactional
    public ResponseEntity<HttpStatus> createJob(JobPostDto jobPostDto){
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            String name = context.getAuthentication().getName();
            if (name == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            UserEntity employer = userRepo.findByUserName(name);

            JobPostEntity job = JobPostEntity.builder().title(jobPostDto.getTitle())
                    .companyName(jobPostDto.getCompanyName())
                    .description(jobPostDto.getDescription())
                    .minSalary(jobPostDto.getMinSalary())
                    .maxSalary(jobPostDto.getMaxSalary())
                    .techStack(jobPostDto.getTechStack())
                    .status(jobPostDto.getStatus())
                    .employerDetails(employer)
                    .build();
            jobPostRepo.save(job);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while creating post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<HttpStatus> changePostEntry(JobPostDto newEntry, Long id){
        JobPostEntity oldEntry = jobPostRepo.findById(id).orElseThrow(() ->{ log.error("Job Post Not Found With Id: {}", id);
            return new NullPointerException();});
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity employer = userRepo.findByUserName(name);
        try {
            if (oldEntry.getEmployerDetails() != null && oldEntry.getEmployerDetails().getId().equals(employer.getId())) {
                if (StringUtils.hasText(newEntry.getTitle())) oldEntry.setTitle(newEntry.getTitle());
                if (StringUtils.hasText(newEntry.getCompanyName())) oldEntry.setCompanyName(newEntry.getCompanyName());
                if (StringUtils.hasText(newEntry.getDescription())) oldEntry.setDescription(newEntry.getDescription());
                if (newEntry.getMinSalary() != null && newEntry.getMinSalary() >= 0)
                    oldEntry.setMinSalary(newEntry.getMinSalary());
                if (newEntry.getMaxSalary() != null && newEntry.getMaxSalary() >= 0)
                    oldEntry.setMaxSalary(newEntry.getMaxSalary());
                if (newEntry.getTechStack() != null && !newEntry.getTechStack().isEmpty())
                    oldEntry.setTechStack(newEntry.getTechStack());
                if (StringUtils.hasText(newEntry.getStatus())) oldEntry.setStatus(newEntry.getStatus());
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

    public ResponseEntity<List<JobApplyDto>> getApplicantsById(Long id){
        JobPostEntity jobOfApplicants = jobPostRepo.findById(id).orElseThrow(() ->{ log.error("Job Post Not Found With Id {}", id);
            return new NullPointerException();});
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity employer = userRepo.findByUserName(name);
        try {
            if (jobOfApplicants.getEmployerDetails() != null && jobOfApplicants.getEmployerDetails().getId().equals(employer.getId())) {
                List<JobApplyEntity> applicants = jobOfApplicants.getApplications();
                List<JobApplyDto> build = new ArrayList<>();
                for (JobApplyEntity applyDto : applicants) {
                    build.add(JobApplyDto.builder()
                            .coverLetterMessage(applyDto.getCoverLetterMessage())
                            .build());
                }
                return new ResponseEntity<>(build, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("No post found for User: {} with this ID: {}.", name, id, e);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }
}
