package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.mapper.JobPostMapper;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class JobSearchSvc {

    private final JobPostRepo jobPostRepo;
    private final JobPostMapper jobPostMapper;

    public List<JobPostDto.ShowAllPostsResponse> customSearch(String techStack, Integer minSalary) {
        List<JobPostEntity> jobs = jobPostRepo.customJobsSearch(techStack, minSalary);
        return jobPostMapper.toShowAllPostsResponse(jobs);
    }
}
