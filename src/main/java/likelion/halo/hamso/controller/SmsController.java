package likelion.halo.hamso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import likelion.halo.hamso.dto.alert.MessageDto;
import likelion.halo.hamso.dto.alert.SmsResponseDto;
import likelion.halo.hamso.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
public class SmsController {
    private final SmsService smsService;

    @GetMapping("/send")
    public String getSmsPage() {
        return "sendSms";
    }

    @PostMapping("/sms/send")
    public String sendSms(MessageDto messageDto, Model model) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        SmsResponseDto response = smsService.sendSms(messageDto);
        model.addAttribute("response", response);
        return "result";
    }
}
