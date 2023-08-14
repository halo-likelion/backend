package likelion.halo.hamso.controller;

import likelion.halo.hamso.argumentresolver.Login;
import likelion.halo.hamso.dto.member.MemberJoinDto;
import likelion.halo.hamso.dto.member.MemberLoginDto;
import likelion.halo.hamso.dto.security.FindLoginIdDto;
import likelion.halo.hamso.dto.security.FindPasswordDto;
import likelion.halo.hamso.dto.security.TokenInfoDto;
import likelion.halo.hamso.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

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

    @GetMapping("/duplicate")
    public ResponseEntity<Boolean> checkLoginIdDuplicate(@RequestParam String loginId) {
        Boolean check = authService.checkDuplicate(loginId);
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    @PostMapping("find-pwd")
    public ResponseEntity<Boolean> findAndUpdatePassword(@RequestBody FindPasswordDto findPasswordDto) {
        Boolean success = authService.findAndUpdatePassword(findPasswordDto.getNewPassword(), findPasswordDto.getPhoneNo());
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    @PostMapping("find-id")
    public ResponseEntity<String> findLoginId(@RequestBody FindLoginIdDto findLoginIdDto) {
        String loginId = authService.findLoginId(findLoginIdDto.getName(), findLoginIdDto.getPhoneNo());
        return new ResponseEntity<>(loginId, HttpStatus.OK);
    }

}
