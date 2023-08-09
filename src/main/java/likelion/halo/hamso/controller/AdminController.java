package likelion.halo.hamso.controller;

import likelion.halo.hamso.argumentresolver.Login;
import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.agriculture.RegionMachineDto;
import likelion.halo.hamso.dto.member.MemberDto;
import likelion.halo.hamso.dto.member.MemberLoginDto;
import likelion.halo.hamso.dto.member.MemberUpdateAllDto;
import likelion.halo.hamso.dto.reservation.*;
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
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ReservationService reservationService;
    private final AgricultureService agricultureService;
    private final MemberService memberService;

    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> getMemberList() {
        List<MemberDto> members = memberService.findAll();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }


    @PutMapping("/deposit")
    public ResponseEntity<Boolean> updateDepositStatus(@RequestParam("reservationId") Long reservationId) {
        return new ResponseEntity<>(reservationService.updateDepositStatus(reservationId), HttpStatus.OK);
    }

    @GetMapping("/reserve/list/{regionId}")
    public ResponseEntity<List<ReservationAdminInfoDto>> getReservationAdminInfoList(@PathVariable("regionId") Long regionId) {
        return new ResponseEntity<>(reservationService.getReservationAdminInfoList(regionId), HttpStatus.OK);
    }

    @PostMapping("/reserve/status")
    public ResponseEntity<ReservationStatus> updateReservationStatus(@RequestBody ReservationStatusDto reservationStatusInfo) {
        return new ResponseEntity<>(reservationService.updateReservationStatus(reservationStatusInfo.getReservationId(), reservationStatusInfo.getReservationStatus()), HttpStatus.OK);
    }


}
