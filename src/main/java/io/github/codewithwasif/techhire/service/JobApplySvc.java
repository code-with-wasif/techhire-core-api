package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.exception.ResourceNotFoundException;
import io.github.codewithwasif.techhire.mapper.JobApplyMapper;
import io.github.codewithwasif.techhire.repository.JobApplyRepo;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import io.github.codewithwasif.techhire.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JobApplySvc {

    private final JobPostRepo jobPostRepo;
    private final JobApplyRepo jobApplyRepo;
    private final SecurityUtils securityUtils;
    private final JobApplyMapper jobApplyMapper;

    @Transactional
    public JobApplyDto.ApplicationResponse applyJob(Long id, JobApplyDto.CreateApplicationRequest createApplicationRequest){
        JobPostEntity jobToApply = jobPostRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job post not found with ID: " + id));

        UserEntity applicant = securityUtils.getCurrentUser();

        JobApplyEntity application = jobApplyMapper.toEntity(createApplicationRequest, jobToApply, applicant);

        JobApplyEntity savedApplication = jobApplyRepo.save(application);

        return jobApplyMapper.toResponse(savedApplication);
        }

    }

