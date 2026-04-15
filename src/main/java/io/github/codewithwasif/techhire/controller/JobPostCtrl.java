package io.github.codewithwasif.techhire.controller;


import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.dto.JobPostDto;
import io.github.codewithwasif.techhire.service.JobPostSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employer")
@Tag(name = "Employer Job Management",
        description = "Endpoints for employers to create, update, delete job posts and view applicants.")
@RequiredArgsConstructor
public class JobPostCtrl {

    private final JobPostSvc jobPostSvc;

    @Operation(
            summary = "Create a new job post",
            description = "Allows an employer to create a new job posting by providing job details.")
    @PostMapping("/post-job")
    public ResponseEntity<HttpStatus> createJob(@Valid @RequestBody JobPostDto jobPostDto){
        return jobPostSvc.createJob(jobPostDto);
    }

    @Operation(
            summary = "Update an existing job post",
            description = "Enables an employer to update the details of a job post by specifying its ID.")
    @PutMapping("/update-entry/{id}")
    public ResponseEntity<HttpStatus> changePostEntry(@RequestBody JobPostDto jobPostDto, @PathVariable String id){
       return jobPostSvc.changePostEntry(jobPostDto, id);
    }

    @Operation(
            summary = "Delete a job post",
            description = "Allows an employer to delete a job post by providing its ID.")
    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<HttpStatus> deleteJobPost(@PathVariable String id){
        return jobPostSvc.deleteJobPost(id);
    }

    @Operation(
            summary = "Get all applicants for a job",
            description = "Lists all applicants who applied for a specific job post by its ID.")
    @GetMapping("/get-all-applicants/{id}")
    public ResponseEntity<List<JobApplyDto>> getApplicantsById(@PathVariable String id){
       return jobPostSvc.getApplicantsById(id);
    }
}
