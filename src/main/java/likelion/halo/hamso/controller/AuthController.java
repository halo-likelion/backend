package likelion.halo.hamso.controller;

import jakarta.servlet.http.HttpServletRequest;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.dto.Member.MemberJoinDto;
import likelion.halo.hamso.dto.Member.MemberLoginDto;
import likelion.halo.hamso.dto.TokenInfoDto;
import likelion.halo.hamso.security.JwtTokenProvider;
import likelion.halo.hamso.service.AuthService;
import likelion.halo.hamso.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;


    @PostMapping("/join")
    public ResponseEntity<String> insertMember(@RequestBody MemberJoinDto memberInfo) {
        String memberLoginId = authService.join(memberInfo);
        return new ResponseEntity<>(memberLoginId, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenInfoDto> login(@RequestBody MemberLoginDto memberInfo) {
        TokenInfoDto tokenInfo = authService.login(memberInfo);
        return ResponseEntity.ok().body(tokenInfo);
    }

}
