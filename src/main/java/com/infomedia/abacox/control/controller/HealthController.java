package com.infomedia.abacox.control.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@RestController
@Tag(name = "Health", description = "Health controller")
@RequestMapping("/control/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity healthCheck() {
        return ResponseEntity.ok(null);
    }
}
