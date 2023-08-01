package likelion.halo.hamso.controller;

import likelion.halo.hamso.dto.member.MemberDto;
import likelion.halo.hamso.dto.member.MemberLoginDto;
import likelion.halo.hamso.dto.member.MemberUpdateAllDto;
import likelion.halo.hamso.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> getMemberList() {
        List<MemberDto> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<MemberDto> getMemberByLoginId(@PathVariable("loginId") String loginId) {
        MemberDto member = memberService.findByLoginId(loginId);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{loginId}")
    public ResponseEntity<Void> deleteMemberByLoginId(@PathVariable("loginId") String loginId) {
        memberService.deleteMember(loginId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-all")
    public ResponseEntity<Void> updateMemberAll(@RequestBody MemberUpdateAllDto memberInfo) {
        memberService.updateMemberAll(memberInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody MemberLoginDto memberInfo) {
        memberService.updatePassword(memberInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
