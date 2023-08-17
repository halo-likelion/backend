package likelion.halo.hamso.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/security")
public class SecurityTestController {

    @PostMapping
    public ResponseEntity<String> securityTest() {
        return ResponseEntity.ok().body("Security Test Success!");
    }
}
