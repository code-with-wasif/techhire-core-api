package io.github.codewithwasif.techhire.controller;

import io.github.codewithwasif.techhire.dto.ResumeDto;
import io.github.codewithwasif.techhire.service.ResumeSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev")
@Tag(
        name = "Upload Resume",
        description = "Endpoint for developers to create resume for the job.")
@RequiredArgsConstructor
public class ResumeCtrl {

    private final ResumeSvc resumeSvc;

    @Operation(
            summary = "Create the resume",
            description = "Allows an developer to create resume for the job")
    @PostMapping("/upload-resume")
    public ResponseEntity<HttpStatus> uploadResume(@Valid @RequestBody ResumeDto.CreateResumeRequest createRequest) {
        return resumeSvc.uploadResume(createRequest);
    }

    @Operation(
            summary = "Get all the resumes",
            description = "Allows an developer to see all his resumes")
    @GetMapping("/get-resumes")
    public ResponseEntity<List<ResumeDto.ResumeResponse>> getResumes() {
        List<ResumeDto.ResumeResponse> resumes = resumeSvc.getResumes();
        return ResponseEntity.status(HttpStatus.OK).body(resumes);
    }
}
