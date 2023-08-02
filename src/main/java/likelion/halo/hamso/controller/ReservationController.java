package likelion.halo.hamso.controller;

import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineUpdateDto;
import likelion.halo.hamso.dto.agriculture.RegionInfoDto;
import likelion.halo.hamso.dto.reservation.ReservationCheckDto;
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
@RequestMapping("/reserve")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/check-possible")
    public ResponseEntity<Boolean> checkReservePossible(@RequestBody ReservationCheckDto reservationCheckDto) {
        LocalDateTime date = LocalDateTime.of(reservationCheckDto.getYear(), reservationCheckDto.getMonth(), reservationCheckDto.getDay(), 0, 0);
        Boolean possibleCheck = reservationService.checkReservePossible(reservationCheckDto.getMachineId(), date);
        return new ResponseEntity<>(possibleCheck, HttpStatus.OK);
    }

}
