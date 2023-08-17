package likelion.halo.hamso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import likelion.halo.hamso.dto.alert.MessageDto;
import likelion.halo.hamso.dto.alert.SmsResponseDto;
import likelion.halo.hamso.exception.MemberDuplicateException;
import likelion.halo.hamso.service.MemberService;
import likelion.halo.hamso.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


//@Controller
//@RequiredArgsConstructor
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alert")
public class SmsController {
    private final SmsService smsService;
    private final MemberService memberService;
    private final HttpSession session;

    @PostMapping("phoneAuth")
    public ResponseEntity<String> phoneAuth(@RequestBody MessageDto messageDto) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if(memberService.checkPhoneNoDuplicate(messageDto.getTo())) {
            throw new MemberDuplicateException("해당 전화번호는 이미 이전에 인증된 휴대전화입니다.");
        };
        String code = smsService.sendRandomMessage();
        messageDto.setContent("[" + code + "]" + "<렛츠-농사>인증번호를 3분 내에 입력해주세요.");
        SmsResponseDto response = smsService.sendSms(messageDto);

//        session.setAttribute("rand", code);

        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @GetMapping("phoneAuthOk")
    @ResponseBody
    public ResponseEntity<Boolean> phoneAuthOk(@RequestParam String code) {
        String rand = (String) session.getAttribute("rand");

        log.info("rand = {}, code = {}", rand, code);

        if (rand.equals(code)) {
            session.removeAttribute("rand");
            return new ResponseEntity<>(true, HttpStatus.OK);
        }

        return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
