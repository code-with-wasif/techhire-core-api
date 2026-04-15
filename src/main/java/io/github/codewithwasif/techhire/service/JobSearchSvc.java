package io.github.codewithwasif.techhire.service;

import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.entity.JobPostEntity;
import io.github.codewithwasif.techhire.repository.JobApplyRepoImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JobSearchSvc {

    private final JobApplyRepoImpl jobApplyRepoimpl;

    public ResponseEntity<List<JobPostDto>> customSearch(String techStack, Integer minSalary){
        List<JobPostEntity> employerEntities = jobApplyRepoimpl.customJobsSearch(techStack, minSalary);
        List<JobPostDto> result = new ArrayList<>();
        if (!employerEntities.isEmpty()) {
            for (JobPostEntity all:employerEntities) {
                result.add(JobPostDto.builder()
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
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
