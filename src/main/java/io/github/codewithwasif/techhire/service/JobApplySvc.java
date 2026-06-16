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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class JobApplySvc {

    private final JobPostRepo jobPostRepo;
    private final JobApplyRepo jobApplyRepo;
    private final UserRepo userRepo;

    @Transactional
    public ResponseEntity<HttpStatus> applyJob(Long id, JobApplyDto jobApplyDto){
        JobPostEntity jobToApply = jobPostRepo.findById(id).orElseThrow(() -> {
            log.error("Job Not Found With Id: {}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Job Post Not Found");
        });
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity applicant = userRepo.findByUserName(currentUserName);
        if (applicant == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try {
            JobApplyEntity application = JobApplyEntity.builder()
                    .coverLetterMessage(jobApplyDto.getCoverLetterMessage())
                    .applicationStatus("PENDING")
                    .jobDetails(jobToApply)
                    .applicantDetails(applicant)
                    .build();

            jobApplyRepo.save(application);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error While Applying Job", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
