package io.github.codewithwasif.techhire.controller;

import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.service.JobSearchSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Job Search & Recommendations",
        description = "Endpoints for exploring active job listings and fetching personalized AI recommendations."
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/jobs")
public class JobSearchCtrl {

    private final JobSearchSvc jobSearchSvc;

    @Operation(
            summary = "Search active job listings",
            description = "Search active jobs using optional filters like technology keyword or minimum salary. Returns all active jobs if no query parameters are given."
    )
    @GetMapping("/search")
    public ResponseEntity<List<JobPostDto.ShowAllPostsResponse>> customSearch(@Parameter(description = "Tech keyword filter (e.g. 'Java', 'React')", example = "Java")
                                                                                  @RequestParam(required = false) String techStack,
                                                                              @Parameter(description = "Minimum starting salary filter", example = "80000")
                                                                              @RequestParam(required = false) Integer minSalary){

        List<JobPostDto.ShowAllPostsResponse> responses = jobSearchSvc.customSearch(techStack, minSalary);
        return ResponseEntity.ok(responses);
    }


    @Operation(
            summary = "Get AI-matched job recommendations",
            description = "Retrieves top recommended jobs for the authenticated developer based on profile similarity."
    )
    @GetMapping("/recommended")
    public List<JobPostDto.ShowAllPostsResponse> recommendedJobs(@Parameter(description = "Maximum number of recommendations to fetch (Default: 10, Max: 30)",
                                                                             example = "10")
                                                                     @RequestParam(defaultValue = "10") int limit){
        return jobSearchSvc.recommendedJobs(limit);
    }
}
