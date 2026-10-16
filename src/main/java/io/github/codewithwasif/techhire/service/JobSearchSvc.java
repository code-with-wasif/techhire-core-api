package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.repository.JobPostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JobSearchSvc {

    private final JobPostRepo jobPostRepo;

    public List<JobPostDto.JobPostResponse> customSearch(String techStack, Integer minSalary) {
        List<JobPostEntity> employerEntities = jobPostRepo.customJobsSearch(techStack, minSalary);
        List<JobPostDto.JobPostResponse> result = new ArrayList<>();

            for (JobPostEntity all : employerEntities) {
                result.add(JobPostDto.JobPostResponse.builder()
                        .id(all.getId())
                        .title(all.getTitle())
                        .companyName(all.getCompanyName())
                        .description(all.getDescription())
                        .minSalary(all.getMinSalary())
                        .maxSalary(all.getMaxSalary())
                        .techStack(all.getTechStack())
                        .status(all.getStatus())
                        .build());
            }

        return result;
    }
}
