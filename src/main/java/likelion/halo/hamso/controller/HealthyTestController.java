package likelion.halo.hamso.controller;

import likelion.halo.hamso.dto.member.MemberJoinDto;
import likelion.halo.hamso.dto.member.MemberLoginDto;
import likelion.halo.hamso.dto.security.FindPasswordDto;
import likelion.halo.hamso.dto.security.TokenInfoDto;
import likelion.halo.hamso.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class HealthyTestController {


    @GetMapping
    public ResponseEntity<Void> checkHealthPath() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
