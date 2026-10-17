package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.entity.JobApplyEntity;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.entity.ResumeEntity;
import io.github.codewithwasif.techhire.entity.UserEntity;
import io.github.codewithwasif.techhire.mapper.JobPostMapper;
import io.github.codewithwasif.techhire.repository.ResumeRepo;
import org.springframework.security.access.AccessDeniedException;
import io.github.codewithwasif.techhire.exception.BadRequestException;
import io.github.codewithwasif.techhire.exception.ResourceNotFoundException;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import io.github.codewithwasif.techhire.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class JobPostSvc {

    private final JobPostRepo jobPostRepo;
    private final SecurityUtils securityUtils;
    private final JobPostMapper jobPostMapper;
    private final ResumeRepo resumeRepo;

    @Transactional
    public JobPostDto.JobPostResponse createJob(JobPostDto.CreateJobPostRequest createRequest){
        validateSalaryBounds(createRequest.minSalary(), createRequest.maxSalary());

        UserEntity employer = securityUtils.getCurrentUser();

        JobPostEntity job = jobPostMapper.toEntity(createRequest, employer);

        JobPostEntity savedPost = jobPostRepo.save(job);
        return jobPostMapper.toResponse(savedPost);
    }

    public List<JobPostDto.JobPostResponse> getMyJobs(){
        UserEntity user = securityUtils.getCurrentUser();
        Long userId = user.getId();

        List<JobPostEntity> allJobsById = jobPostRepo.getAllJobsById(userId);

        return jobPostMapper.toGetAllPosts(allJobsById);
    }

    @Transactional
    public JobPostDto.JobPostResponse changePostEntry(JobPostDto.UpdateJobPostRequest request, Long id){
        JobPostEntity existingPost = fetchJobPostById(id);
        UserEntity employer = securityUtils.getCurrentUser();
        validateOwnership(existingPost, employer);

        int updatedMin = request.minSalary() != null ? request.minSalary() : existingPost.getMinSalary();
        int updatedMax = request.maxSalary() != null ? request.maxSalary() : existingPost.getMaxSalary();
        validateSalaryBounds(updatedMin, updatedMax);

        jobPostMapper.updateEntityFromDto(request, existingPost);

        JobPostEntity savedPost = jobPostRepo.saveAndFlush(existingPost);
        return jobPostMapper.toResponse(savedPost);
        }

    @Transactional
    public void deleteJobPost(Long id) {

        UserEntity employer = securityUtils.getCurrentUser();
        JobPostEntity jobToDelete = fetchJobPostById(id);
        validateOwnership(jobToDelete, employer);
        jobPostRepo.delete(jobToDelete);
    }

    public List<JobPostDto.ApplicantReviewResponse> getApplicantsById(Long id){
        JobPostEntity jobOfApplicants = fetchJobPostById(id);
        UserEntity employer = securityUtils.getCurrentUser();
        validateOwnership(jobOfApplicants, employer);

        List<JobApplyEntity> applicants = jobOfApplicants.getApplications();

        List<Long> resumeId = new ArrayList<>();
        for(JobApplyEntity key : applicants){
            resumeId.add(key.getResumeId());
        }

        List<ResumeEntity> resumes = resumeRepo.findAllById(resumeId);
        Map<Long, ResumeEntity> resumeMap = new HashMap<>();
        for(ResumeEntity resume : resumes){
            resumeMap.put(resume.getId(), resume);
        }

        return jobPostMapper.toApplicantReviewResponse(applicants, resumeMap);
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

    private void validateSalaryBounds(Integer minSalary, Integer maxSalary) {
        if (minSalary != null && maxSalary != null && minSalary > maxSalary) {
            throw new BadRequestException("Minimum salary cannot exceed maximum salary.");
        }
    }

}