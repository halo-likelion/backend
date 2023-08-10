package likelion.halo.hamso.controller;

import jakarta.servlet.http.HttpSession;
import likelion.halo.hamso.service.MemberService;
import likelion.halo.hamso.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
//@RequestMapping("/alert")
public class AlertController {

    private final MemberService memberService;
    private final SmsService smsService;
    private final HttpSession session;

    @PostMapping("phoneAuth")
    public Boolean phoneAuth(@RequestBody String phoneNo) {
        try { // 이미 가입된 전화번호가 있으면
            return memberService.checkPhoneNoDuplicate(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code = smsService.sendRandomMessage();
        session.setAttribute("rand", code);

        return false;
    }

    @PostMapping("phoneAuthOk")
    @ResponseBody
    public Boolean phoneAuthOk(@RequestParam String code) {
        String rand = (String) session.getAttribute("rand");

        System.out.println(rand + " : " + code);

        if (rand.equals(code)) {
            session.removeAttribute("rand");
            return false;
        }

        return true;
    }
}
