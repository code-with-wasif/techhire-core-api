package io.github.codewithwasif.techhire.controller;

import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.service.JobSearchSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(
        name = "Job Search",
        description = "Endpoint for public to explore and filter active job opportunities.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/jobs")
public class JobSearchCtrl {

    private final JobSearchSvc jobSearchSvc;

    @Operation(summary = "Discover active jobs",
            description = "Use optional query parameters to filter the results. If no parameters are provided, it returns all currently active jobs.")
    @GetMapping
    public ResponseEntity<List<JobPostDto>> customSearch(@RequestParam(required = false) String techStack,
                                                         @RequestParam(required = false) Integer
                                                                    minSalary){
        return jobSearchSvc.customSearch(techStack, minSalary);
    }
}
