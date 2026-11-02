package io.github.codewithwasif.techhire.controller;

import io.github.codewithwasif.techhire.dto.DeveloperProfileDto;
import io.github.codewithwasif.techhire.service.DeveloperProfileSvc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Developer Profile",
        description = "Endpoints for managing developer profiles and candidate details."
)

@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DeveloperProfileCtrl {
    private final DeveloperProfileSvc developerProfileSvc;

    @Operation(
            summary = "Create developer profile",
            description = "Creates a new profile for the logged-in user and generates its vector embedding. Enforces a 1-to-1 relationship per account."
    )
    @PostMapping("/profile")
    public ResponseEntity<DeveloperProfileDto.DeveloperProfileResponse> createProfile(
            @Valid @RequestBody DeveloperProfileDto.CreateDeveloperProfileRequest request
    ) {
        DeveloperProfileDto.DeveloperProfileResponse response = developerProfileSvc.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
