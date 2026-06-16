package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.ResumeDto;
import io.github.codewithwasif.techhire.entity.ResumeEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.repository.ResumeRepo;
import io.github.codewithwasif.techhire.repository.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResumeSvc {
    private final UserRepo userRepo;
    private final ResumeRepo resumeRepo;

    @Transactional
    public ResponseEntity<HttpStatus> uploadResume(ResumeDto resumeDto){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity candidate = userRepo.findByUserName(userName);
        try {
            ResumeEntity resume = ResumeEntity.builder()
                    .fullName(resumeDto.getFullName())
                    .professionalTitle(resumeDto.getProfessionalTitle())
                    .skills(resumeDto.getSkills())
                    .portfolioUrl(resumeDto.getPortfolioUrl())
                    .bio(resumeDto.getBio())
                    .candidateDetails(candidate)
                    .build();
            resumeRepo.save(resume);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error While Creating Resume", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
