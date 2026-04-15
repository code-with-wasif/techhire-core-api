package io.github.codewithwasif.techhire.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemCtrl {

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck(){
        return new ResponseEntity<>("Server is active", HttpStatus.OK);
    }
}
