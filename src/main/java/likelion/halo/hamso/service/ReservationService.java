package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.*;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineUpdateDto;
import likelion.halo.hamso.dto.agriculture.RegionInfoDto;
import likelion.halo.hamso.dto.member.MemberDto;
import likelion.halo.hamso.dto.reservation.ReservationLogDto;
import likelion.halo.hamso.dto.reservation.ReservationLogSpecificDto;
import likelion.halo.hamso.exception.MemberDuplicateException;
import likelion.halo.hamso.exception.NotEnoughCntException;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReservationService {
    private final AgriMachineRepository agriMachineRepository;
    private final AgriRegionRepository agriRegionRepository;
    private final ReservationRepository reservationRepository;
    private final PossibleRepository possibleRepository;
    private final MemberRepository memberRepository;

    public Boolean checkReservePossible(AgriMachineType machineType, Long regionId, LocalDateTime date){ // 해당 날짜에 해당 농기계 예약 가능여부 반환
        Optional<AgriMachine> oMachine = agriMachineRepository.findByTypeAndRegion(machineType, regionId);
        AgriPossible possible = possibleRepository.getMachineDateInfo(oMachine.get().getId(), date);
        return possible.getReservePossible();
    }

    public Boolean checkReservePossible(Long machineId, LocalDateTime date){ // 해당 날짜에 해당 농기계 예약 가능여부 반환
        AgriPossible possible = possibleRepository.getMachineDateInfo(machineId, date);
        return possible.getReservePossible();
    }

    @Transactional
    public Integer removeCnt(Long machineId, LocalDateTime date) {
        AgriPossible possible = possibleRepository.getMachineDateInfo(machineId, date);
        int cnt = possible.getCnt();
        cnt--;
        if (cnt < 0) {
            throw new NotEnoughCntException("No more stock to reserve");
        }
        if (cnt == 0) {
            possible.setReservePossible(false);
        }
        possible.setCnt(cnt);
        return possible.getCnt();
    }

    @Transactional
    public Long makeReservation(Reservation reservation) {
        log.info("예약 시작: {}", reservation);
        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("예약 완료: {}", savedReservation);
        return savedReservation.getId();
    }


    public List<ReservationLogDto> getReservationLogList(String loginId) {
        Optional<List<Reservation>> reservationList = reservationRepository.getReservationListByLoginId(loginId);
        if (reservationList.isEmpty()) {
            throw new NotFoundException("예약 내역이 존재하지 않습니다.");
        }

        return convertReservationToReservationDto(reservationList.get());
    }

    private static List<ReservationLogDto> convertReservationToReservationDto(List<Reservation> reservationList) {
        List<ReservationLogDto> reservationLogDtoList = reservationList.stream()
                .map(a -> new ReservationLogDto(a))
                .collect(Collectors.toList());
        return reservationLogDtoList;
    }

    public List<ReservationLogSpecificDto> getReservationLogSpecificList(String loginId) {
        Optional<List<Reservation>> reservationList = reservationRepository.getReservationListByLoginId(loginId);
        if (reservationList.isEmpty()) {
            throw new NotFoundException("예약 내역이 존재하지 않습니다.");
        }

        return convertReservationToReservationSpecificDto(reservationList.get());
    }

    private static List<ReservationLogSpecificDto> convertReservationToReservationSpecificDto(List<Reservation> reservationList) {
        List<ReservationLogSpecificDto> reservationLogDtoList = reservationList.stream()
                .map(a -> new ReservationLogSpecificDto(a))
                .collect(Collectors.toList());
        return reservationLogDtoList;
    }

    @Transactional
    public Boolean updateDepositStatus(Long reservationId) {
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isEmpty()) {
            throw new NotFoundException("해당 예약 내역은 존재하지 않습니다.");
        }
        Reservation reservation = oReservation.get();
        if(reservation.getDeposit()) {
            reservation.setDeposit(false);
        } else {
            reservation.setDeposit(true);
        }
        return reservation.getDeposit();
    }

    public Integer[] getPossibleMonthArray(Long machineId) {
        LocalDateTime now = LocalDateTime.now();
        int lastDayOfMonth = YearMonth.of(now.getYear(), now.getMonth()).lengthOfMonth();
        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonth(), lastDayOfMonth, 23, 59);
        List<AgriPossible> possibleList = possibleRepository.getPossibleList(machineId, start, end);
        Integer[] arr = new Integer[lastDayOfMonth];
        for(int i=0;i<arr.length;i++) {
            if(possibleList.get(i).getReservePossible()) {
                arr[i] = 1;
            } else{
                arr[i] = 0;
            }
        }
        return arr;
    }
}
