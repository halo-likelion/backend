package likelion.halo.hamso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.agriculture.AdminMachineInfoDto;
import likelion.halo.hamso.dto.alert.MessageDto;
import likelion.halo.hamso.dto.alert.SmsResponseDto;
import likelion.halo.hamso.dto.member.MemberDto;
import likelion.halo.hamso.dto.reservation.*;
import likelion.halo.hamso.service.AgricultureService;
import likelion.halo.hamso.service.MemberService;
import likelion.halo.hamso.service.ReservationService;
import likelion.halo.hamso.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final ReservationService reservationService;
    private final AgricultureService agricultureService;
    private final MemberService memberService;
    private final SmsService smsService;


    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> getMemberList() {
        List<MemberDto> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }


    @PutMapping("/deposit")
    public ResponseEntity<Boolean> updateDepositStatus(@RequestParam("reservationId") Long reservationId) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        Reservation reservation = reservationService.findReservationById(reservationId);

        Boolean depositStatus = reservationService.updateDepositStatus(reservationId);
        if(depositStatus) {
            Member member = reservation.getMember();
            MessageDto messageDto = new MessageDto();
            messageDto.setTo(member.getPhoneNo());
            messageDto.setContent("<렛츠-농사> " + member.getName() +"님 "+ reservation.getAgriMachine().getType()+"이(가) 입금 확인되어 예약 신청 확정되셨습니다.");
            SmsResponseDto response = smsService.sendSms(messageDto);
            log.info("message log = {}", response);
            reservationService.updateReservationStatus(reservationId, ReservationStatus.RESERVED);
        }

        return new ResponseEntity<>(depositStatus, HttpStatus.OK);
    }

    @GetMapping("/reserve/list/{regionId}")
    public ResponseEntity<List<ReservationAdminInfoDto>> getReservationAdminInfoList(@PathVariable("regionId") Long regionId) {
        return new ResponseEntity<>(reservationService.getReservationAdminInfoList(regionId), HttpStatus.OK);
    }

    @PostMapping("/reserve/update")
    public ResponseEntity<ReservationStatus> updateReserveList(@RequestBody ReservationUpdateDto reservationStatusInfo) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if(reservationStatusInfo.getReservationStatus().equals(ReservationStatus.CANCELED)) {
            throw new RuntimeException("취소된 예약 상태는 수정이 더 이상 불가능합니다.");
        }
        Long reservationId = reservationStatusInfo.getReservationId();
        if(reservationStatusInfo.getReservationStatus().equals(ReservationStatus.RESERVED)) {
            Reservation reservation = reservationService.findReservationById(reservationId);

            Boolean depositStatus = reservationService.updateDepositStatus(reservationId);
            if(depositStatus) {
                Member member = reservation.getMember();
                MessageDto messageDto = new MessageDto();
                messageDto.setTo(member.getPhoneNo());
                messageDto.setContent("<렛츠-농사> " + member.getName() +"님 "+ reservation.getAgriMachine().getType()+"이(가) 입금 확인되어 예약 신청 확정되셨습니다.");
                SmsResponseDto response = smsService.sendSms(messageDto);
                log.info("message log = {}", response);
                reservationService.updateReservationStatus(reservationId, ReservationStatus.RESERVED);
            }
        }
        return new ResponseEntity<>(reservationService.updateReservationStatus(reservationId, reservationStatusInfo.getReservationStatus()), HttpStatus.OK);
    }

    @GetMapping("/machine-list")
    public ResponseEntity<List<AdminMachineInfoDto>> getAdminMachineInfoDto(@RequestParam Long regionId) {
        List<AdminMachineInfoDto> adminMachineInfoDtoList = agricultureService.getAdminMachineInfoList(regionId);
        return new ResponseEntity<>(adminMachineInfoDtoList, HttpStatus.OK);
    }


}
