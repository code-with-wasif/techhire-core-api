package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import io.github.codewithwasif.techhire.repository.JobApplyRepo;
import io.github.codewithwasif.techhire.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobApplySvc {

    private final JobPostRepo jobPostRepo;
    private final JobApplyRepo jobApplyRepo;
    private final UserRepo userRepo;

    public ResponseEntity<HttpStatus> applyJob(String id, JobApplyDto jobApplyDto){
        JobPostEntity employerEntity = jobPostRepo.findById(id).orElseThrow(() -> {
            log.error("Job Not Found With Id: {}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job Post Not Found");
        });
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity repoByUserName = userRepo.findByUserName(currentUserName);
        try {
            JobApplyEntity build = JobApplyEntity.builder()
                    .fullName(jobApplyDto.getFullName())
                    .email(jobApplyDto.getEmail())
                    .resumeLink(jobApplyDto.getResumeLink())
                    .coverLetter(jobApplyDto.getCoverLetter())
                    .githubProfileUrl(jobApplyDto.getGithubProfileUrl())
                    .applicationStatus("PENDING")
                    .userDetails(repoByUserName)
                    .build();


            JobApplyEntity save = jobApplyRepo.save(build);
            if(employerEntity.getApplicants() == null) {
                employerEntity.setApplicants(new ArrayList<>());
            }
            employerEntity.getApplicants().add(save);
            jobPostRepo.save(employerEntity);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While Applying Job", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
