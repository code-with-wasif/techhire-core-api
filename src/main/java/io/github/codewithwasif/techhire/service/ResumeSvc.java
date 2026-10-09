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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResumeSvc {
    private final UserRepo userRepo;
    private final ResumeRepo resumeRepo;

    @Transactional
    public ResponseEntity<HttpStatus> uploadResume(ResumeDto.CreateResumeRequest createRequest){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity candidate = userRepo.findByUserName(userName);
        try {
            ResumeEntity resume = ResumeEntity.builder()
                    .fullName(createRequest.fullName())
                    .professionalTitle(createRequest.professionalTitle())
                    .skills(createRequest.skills())
                    .portfolioUrl(createRequest.portfolioUrl())
                    .bio(createRequest.bio())
                    .candidateDetails(candidate)
                    .build();
            resumeRepo.save(resume);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error While Creating Resume", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public List<ResumeDto.ResumeResponse> getResumes(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity candidate = userRepo.findByUserName(username);
        long id = candidate.getId();
        List<ResumeEntity> resumeList = resumeRepo.getAllResumesById(id);
        List<ResumeDto.ResumeResponse> responseList = new ArrayList<>();
        for (ResumeEntity resume: resumeList){
            ResumeDto.ResumeResponse resumeDto = ResumeDto.ResumeResponse.builder()
                    .id(resume.getId())
                    .fullName(resume.getFullName())
                    .professionalTitle(resume.getProfessionalTitle())
                    .skills(resume.getSkills())
                    .portfolioUrl(resume.getPortfolioUrl())
                    .bio(resume.getBio())
                    .build();
            responseList.add(resumeDto);
        }
        return responseList;
    }

}
