package likelion.halo.hamso.controller;

import likelion.halo.hamso.argumentresolver.Login;
import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.dto.reservation.ReservationCheckDto;
import likelion.halo.hamso.dto.reservation.ReservationInfoDto;
import likelion.halo.hamso.exception.NotAvailableReserveException;
import likelion.halo.hamso.exception.NotLoginException;
import likelion.halo.hamso.service.AgricultureService;
import likelion.halo.hamso.service.MemberService;
import likelion.halo.hamso.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
public class ReservationController {
    private final ReservationService reservationService;
    private final AgricultureService agricultureService;
//    private final MemberService memberService;

    @PostMapping("/check-possible")
    public ResponseEntity<Boolean> checkReservePossible(@RequestBody ReservationCheckDto reservationCheckDto) {
        LocalDateTime wantTime = LocalDateTime.of(reservationCheckDto.getYear(), reservationCheckDto.getMonth(), reservationCheckDto.getDay(), 0, 0);
        Boolean possibleCheck = reservationService.checkReservePossible(reservationCheckDto.getMachineType(), reservationCheckDto.getRegionId(), wantTime);
        return new ResponseEntity<>(possibleCheck, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Long> makeReservation(@RequestBody ReservationInfoDto reservationInfo,
                                                @Login Member loginMember) {
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

        return new ResponseEntity<>(reservation.getId(), HttpStatus.OK);
    }

}
