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
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobPostSvc {

    private final JobPostRepo jobPostRepo;
    private final UserRepo userRepo;

    public ResponseEntity<HttpStatus> createJob(JobPostDto jobPostDto){
        try {
            JobPostEntity job = JobPostEntity.builder().title(jobPostDto.getTitle())
                    .companyName(jobPostDto.getCompanyName())
                    .description(jobPostDto.getDescription())
                    .minSalary(jobPostDto.getMinSalary())
                    .maxSalary(jobPostDto.getMaxSalary())
                    .techStack(jobPostDto.getTechStack())
                    .status(jobPostDto.getStatus())
                    .build();
            jobPostRepo.save(job);
            SecurityContext context = SecurityContextHolder.getContext();
            String name = context.getAuthentication().getName();
            if (name != null){
                UserEntity user = userRepo.findByUserName(name);
                user.getPosts().add(job);
                userRepo.save(user);
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while creating post", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<HttpStatus> changePostEntry(JobPostDto newEntry, String id){
        JobPostEntity oldEntry = jobPostRepo.findById(id).orElseThrow(() ->{ log.error("Job Post Not Found With Id: {}", id);
            return new NullPointerException();});
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity user = userRepo.findByUserName(name);
        try {
            if (user.getPosts().stream().anyMatch(x -> x.getId().equals(id))) {
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
        } catch (Exception e) {
            log.error("No post found for User: {} with this ID: {}.", name, id, e);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<HttpStatus> deleteJobPost(String id){
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        try {
            if (name != null) {
                UserEntity user = userRepo.findByUserName(name);
                boolean removed = user.getPosts().removeIf(x -> x.getId().equals(id));
                if (removed) {
                    userRepo.save(user);
                    jobPostRepo.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            }
        } catch (Exception e) {
            log.error("No post found for User: {} with this ID: {}.", name, id, e);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    public ResponseEntity<List<JobApplyDto>> getApplicantsById(String id){
        JobPostEntity entry = jobPostRepo.findById(id).orElseThrow(() ->{ log.error("Job Post Not Found With Id {}", id);
            return new NullPointerException();});
        SecurityContext context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        UserEntity user = userRepo.findByUserName(name);
        try {
            if (user.getPosts().stream().anyMatch(x -> x.getId().equals(id))) {
                List<JobApplyEntity> applicants = entry.getApplicants();

                List<JobApplyDto> build = new ArrayList<>();
                for (JobApplyEntity applyDto : applicants) {
                    build.add(JobApplyDto.builder()
                            .fullName(applyDto.getFullName())
                            .email(applyDto.getEmail())
                            .resumeLink(applyDto.getResumeLink())
                            .coverLetter(applyDto.getCoverLetter())
                            .githubProfileUrl(applyDto.getGithubProfileUrl())
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
