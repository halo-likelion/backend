package likelion.halo.hamso.controller;

import likelion.halo.hamso.argumentresolver.Login;
import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.dto.agriculture.RegionMachineDto;
import likelion.halo.hamso.dto.reservation.ReservationCheckDto;
import likelion.halo.hamso.dto.reservation.ReservationInfoDto;
import likelion.halo.hamso.dto.reservation.ReservationLogDto;
import likelion.halo.hamso.dto.reservation.ReservationLogSpecificDto;
import likelion.halo.hamso.exception.NotAvailableReserveException;
import likelion.halo.hamso.exception.NotLoginException;
import likelion.halo.hamso.service.AgricultureService;
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



    @PutMapping("/deposit")
    public ResponseEntity<Boolean> updateDepositStatus(@RequestParam("reservationId") Long reservationId) {
        return new ResponseEntity<>(reservationService.updateDepositStatus(reservationId), HttpStatus.OK);
    }

}
