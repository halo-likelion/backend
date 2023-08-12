package likelion.halo.hamso.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import likelion.halo.hamso.argumentresolver.Login;
import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.agriculture.RegionMachineDto;
import likelion.halo.hamso.dto.alert.MessageDto;
import likelion.halo.hamso.dto.alert.SmsResponseDto;
import likelion.halo.hamso.dto.reservation.ReservationCheckDto;
import likelion.halo.hamso.dto.reservation.ReservationInfoDto;
import likelion.halo.hamso.dto.reservation.ReservationLogDto;
import likelion.halo.hamso.dto.reservation.ReservationLogSpecificDto;
import likelion.halo.hamso.exception.MemberDuplicateException;
import likelion.halo.hamso.exception.NotAvailableReserveException;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.exception.NotLoginException;
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
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {
    private final ReservationService reservationService;
    private final AgricultureService agricultureService;
    private final MemberService memberService;
    private final SmsService smsService;

    @PostMapping("/check-possible")
    public ResponseEntity<Boolean> checkReservePossible(@RequestBody ReservationCheckDto reservationCheckDto) {
        LocalDateTime wantTime = LocalDateTime.of(reservationCheckDto.getYear(), reservationCheckDto.getMonth(), reservationCheckDto.getDay(), 0, 0);
        Boolean possibleCheck = reservationService.checkReservePossible(reservationCheckDto.getMachineType(), reservationCheckDto.getRegionId(), wantTime);
        return new ResponseEntity<>(possibleCheck, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Long> makeReservation(@RequestBody ReservationInfoDto reservationInfo,
                                                @Login Member loginMember) throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if(loginMember == null) {
            throw new NotLoginException("로그인이 안되어 있는 상태입니다.");
        }
        log.info("loginMember = {}", loginMember);

        Long machineId = reservationInfo.getMachineId();
        LocalDateTime wantTime = reservationInfo.getWantTime();
        // 예약할 날짜를 보내줬을 때 원래 있던 예약과 겹치는지?
        log.info("checkDuplicateReservation:  예약할 날짜를 보내줬을 때 원래 있던 예약과 겹치는지?");
        // 머신 타입으로 예약 가능하게 변경하기!
        if(!reservationService.checkReservePossible(reservationInfo.getMachineId(), wantTime)) {
            throw new NotAvailableReserveException("예약이 불가능합니다.");
        }

        // 예약 저장
        AgriMachine machine = agricultureService.findByMachineIdReal(machineId);
        Reservation reservation = Reservation.createReservation(reservationInfo, loginMember, machine);
        log.info("reservation = {}", reservation);
        reservationService.makeReservation(reservation);

        // remove Cnt
        reservationService.removeCnt(machineId, wantTime);


        // reservation message sending
        MessageDto messageDto = new MessageDto();
        messageDto.setTo(loginMember.getPhoneNo());

        int year = reservation.getWantTime().getYear();
        Month month = reservation.getWantTime().getMonth();
        int day = reservation.getWantTime().getDayOfMonth();

        messageDto.setContent("<렛츠-농사> " + reservation.getMember().getName() +"님 [" +year + "년" + month.getValue() + "월" + day + "일" +"]에 예약 신청에 성공하셨습니다.");
        SmsResponseDto response1 = smsService.sendSms(messageDto);
        log.info("message log1 = {}", response1);
        messageDto.setContent("예약 확정을 위해서 <계좌번호>로 예약 하신 날짜 당일까지 입금 부탁드립니다 ♡");
        SmsResponseDto response2 = smsService.sendSms(messageDto);
        log.info("message log2 = {}", response2);

        return new ResponseEntity<>(reservation.getId(), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReservationLogDto>> getReservationLogList(@Login Member loginMember) {
        List<ReservationLogDto> reservationLogDtoList = reservationService.getReservationLogList(loginMember.getLoginId());
        return new ResponseEntity<>(reservationLogDtoList, HttpStatus.OK);
    }

    @GetMapping("/list-specific")
    public ResponseEntity<List<ReservationLogSpecificDto>> getReservationLogSpecificList(@Login Member loginMember) {
        List<ReservationLogSpecificDto> reservationLogDtoList = reservationService.getReservationLogSpecificList(loginMember.getLoginId());
        return new ResponseEntity<>(reservationLogDtoList, HttpStatus.OK);
    }

    @PostMapping("/possible/month")
    public ResponseEntity<Integer[]> possibleMonthArray(@RequestBody RegionMachineDto regionMachineDto) {
        return new ResponseEntity<>(reservationService.getPossibleMonthArray(regionMachineDto.getMachineId()), HttpStatus.OK);
    }

    @GetMapping("/cancel/{reservationId}")
    public ResponseEntity<ReservationStatus> cancelReservation(@PathVariable("reservationId") Long reservationId) {
        return new ResponseEntity<>(reservationService.updateReservationStatus(reservationId, ReservationStatus.CANCELED), HttpStatus.OK);
    }

}
