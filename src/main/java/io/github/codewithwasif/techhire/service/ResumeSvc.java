package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.ResumeDto;
import io.github.codewithwasif.techhire.entity.ResumeEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.mapper.ResumeMapper;
import io.github.codewithwasif.techhire.repository.ResumeRepo;
import io.github.codewithwasif.techhire.utils.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ResumeSvc {
    private final ResumeRepo resumeRepo;
    private final SecurityUtils securityUtils;
    private final ResumeMapper resumeMapper;

    @Transactional
    public ResumeDto.ResumeResponse uploadResume(ResumeDto.CreateResumeRequest createRequest){
        UserEntity candidate = securityUtils.getCurrentUser();

        ResumeEntity resume = resumeMapper.toEntity(createRequest, candidate);
        ResumeEntity savedResume = resumeRepo.save(resume);

        return resumeMapper.toResponse(savedResume);
    }

    public List<ResumeDto.ResumeResponse> getResumes(){

        UserEntity candidate = securityUtils.getCurrentUser();
        long id = candidate.getId();

        List<ResumeEntity> resumeList = resumeRepo.getAllResumesById(id);
        return resumeMapper.toResponseList(resumeList);
    }

}
