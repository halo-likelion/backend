package likelion.halo.hamso.controller;

import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.dto.Member.MemberJoinDto;
import likelion.halo.hamso.dto.Member.MemberLoginDto;
import likelion.halo.hamso.service.AuthService;
import likelion.halo.hamso.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;


    @PostMapping("/join")
    public ResponseEntity<String> insertMember(@RequestBody MemberJoinDto memberInfo) {
        String memberLoginId = authService.join(memberInfo);
        return new ResponseEntity<>(memberLoginId, HttpStatus.CREATED);
    }

    @PostMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody MemberLoginDto memberInfo) {
        authService.updatePassword(memberInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
