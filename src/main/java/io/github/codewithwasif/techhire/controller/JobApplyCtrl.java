package io.github.codewithwasif.techhire.controller;

import io.github.codewithwasif.techhire.dto.JobApplyDto;
import io.github.codewithwasif.techhire.service.JobApplySvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev")
@Tag(
        name = "Job Applications",
        description = "Endpoint for developers to apply for a job.")
@RequiredArgsConstructor
public class JobApplyCtrl {

    private final JobApplySvc jobApplySvc;

    @Operation(
            summary = "Apply for a job post",
            description = "Allows an developer to apply for a specific job post by providing its ID and application details.")
    @PostMapping("/job-apply/{id}")
    public ResponseEntity<HttpStatus> applyJob(@PathVariable String id, @Valid @RequestBody JobApplyDto jobApplyDto){
        return jobApplySvc.applyJob(id, jobApplyDto);
    }


}
