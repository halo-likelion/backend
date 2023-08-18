package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.*;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.agriculture.EachMachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineUpdateDto;
import likelion.halo.hamso.dto.agriculture.RegionInfoDto;
import likelion.halo.hamso.dto.each.FindEachMachineDto;
import likelion.halo.hamso.dto.member.MemberDto;
import likelion.halo.hamso.dto.reservation.*;
import likelion.halo.hamso.exception.MemberDuplicateException;
import likelion.halo.hamso.exception.NotEnoughCntException;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.standard.expression.Each;

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
    private final EachMachineRepository eachMachineRepository;
    private final EachMachinePossibleRepository eachMachinePossibleRepository;
    private final EachMachineService eachMachineService;

    public Boolean checkReservePossible(AgriMachineType machineType, Long regionId, LocalDateTime date){ // 해당 날짜에 해당 농기계 예약 가능여부 반환
        Optional<AgriMachine> oMachine = agriMachineRepository.findByTypeAndRegion(machineType, regionId);
        AgriPossible possible = possibleRepository.getMachineDateInfo(oMachine.get().getId(), date);
        return possible.getReservePossible();
    }

    public Boolean checkReservePossible(Long machineId, LocalDateTime date){ // 해당 날짜에 해당 농기계 예약 가능여부 반환
        AgriPossible possible = possibleRepository.getMachineDateInfo(machineId, date);
        return possible.getReservePossible();
    }

    public Boolean checkReserveAllDayPossible(Long machineId, LocalDateTime wantTime, Integer reserveDayCnt){
        // 해당 날짜에 해당 농기계 예약 가능여부 반환
        for(int i=0;i<reserveDayCnt;i++) {
            AgriPossible possible = possibleRepository.getMachineDateInfo(machineId, wantTime.plusDays(i));
            if(!possible.getReservePossible()) return false;

        }
        return true;
    }

    @Transactional
    public void removeCnt(Long machineId, LocalDateTime date, Integer reserveCnt) {
        for(int i=0;i<reserveCnt;i++) {
            AgriPossible possible = possibleRepository.getMachineDateInfo(machineId, date.plusDays(i));
            int cnt = possible.getCnt();
            cnt--;
            if (cnt < 0) {
                throw new NotEnoughCntException("No more stock to reserve");
            }
            if (cnt <= 0) {
                possible.setReservePossible(false);
            }
            possible.setCnt(cnt);
        }
    }

    @Transactional
    public void addCnt(Long machineId, LocalDateTime date, Integer reserveCnt) {
        for(int i=0;i<reserveCnt;i++) {
            AgriPossible possible = possibleRepository.getMachineDateInfo(machineId, date.plusDays(i));
            int cnt = possible.getCnt();
            cnt++;
            if (cnt > 0) {
                possible.setReservePossible(true);
            }
            possible.setCnt(cnt);
        }
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

    public ReservationLogSpecificDto getReservationLogSpecificList(String loginId, Long reservationId) {
        Optional<Reservation> reservation = reservationRepository.getReservationByLoginIdAndReservationId(loginId, reservationId);
        if (reservation.isEmpty()) {
            throw new NotFoundException("예약 내역이 존재하지 않습니다.");
        }

        return new ReservationLogSpecificDto(reservation.get());
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

    public List<ReservationAdminInfoDto> getReservationAdminInfoList(Long regionId) {
        List<Reservation> reservationList = reservationRepository.findByRegionId(regionId);
        List<ReservationAdminInfoDto> reservationAdminInfoDtoList = convertReservationToReservationAdminInfoDto(reservationList);
        return reservationAdminInfoDtoList;
    }

    private static List<ReservationAdminInfoDto> convertReservationToReservationAdminInfoDto(List<Reservation> reservationList) {
        List<ReservationAdminInfoDto> reservationLogDtoList = reservationList.stream()
                .map(a -> new ReservationAdminInfoDto(a))
                .collect(Collectors.toList());
        return reservationLogDtoList;
    }

    @Transactional
    public ReservationStatus updateReservationStatus(ReservationUpdateDto reservationUpdateDto) {
        Long reservationId = reservationUpdateDto.getReservationId();
        Long eachMachineId = reservationUpdateDto.getEachMachineId();
        ReservationStatus reservationStatus = reservationUpdateDto.getReservationStatus();
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isEmpty()) {
            throw new NotFoundException("해당 예약 번호의 예약 내역은 존재하지 않습니다.");
        }
        Reservation reservation = oReservation.get();
        reservation.setStatus(reservationStatus);

        Optional<EachMachine> oEachMachine = eachMachineRepository.findById(eachMachineId);
        if(oEachMachine.isEmpty()) {
            throw new NotFoundException("해당 기계 정보는 존재하지 않습니다.");

        }
        reservation.setEachMachine(oEachMachine.get());
        if(reservationStatus.equals(ReservationStatus.CANCELED)) {
            addCnt(reservation.getAgriMachine().getId(), reservation.getWantTime(), reservation.getReserveDayCnt());
        } else if (reservationStatus.equals(ReservationStatus.RESERVING)) {
            removeCnt(reservation.getAgriMachine().getId(), reservation.getWantTime(), reservation.getReserveDayCnt());
        }
        return reservation.getStatus();
    }

//    @Transactional
//    public ReservationStatus updateReservationStatus(Long reservationId, ReservationStatus reservationStatus, Long eachMachineId) {
//        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
//        if(oReservation.isEmpty()) {
//            throw new NotFoundException("해당 예약 번호의 예약 내역은 존재하지 않습니다.");
//        }
//        Optional<EachMachine> oEachMachine = eachMachineRepository.findById(eachMachineId);
//        if(oEachMachine.isEmpty()) {
//            throw new NotFoundException("해당 기계 정보는 존재하지 않습니다.");
//        }
//        Reservation reservation = oReservation.get();
//        reservation.setStatus(reservationStatus);
//        reservation.setEachMachine(oEachMachine.get());
//        addCnt(reservation.getAgriMachine().getId(), reservation.getWantTime());
//        return reservation.getStatus();
//    }

    public Reservation findReservationById(Long reservationId) {
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isEmpty()) {
            throw new NotFoundException("해당 예약 번호의 예약 내역은 존재하지 않습니다.");
        }
        return oReservation.get();
    }

    public List<Reservation> getReservingReservation() {
        List<Reservation> all = reservationRepository.findStatusReservation(ReservationStatus.RESERVING);
        return all;
    }

    public List<Reservation> getReservedReservation() {
        List<Reservation> all = reservationRepository.findStatusReservation(ReservationStatus.RESERVED);
        return all;
    }


    @Transactional
    public Long assignEachMachine(ReservationAssignEachMachine reservationAssignEachMachine) {
        Long reservationId = reservationAssignEachMachine.getReservationId();
        Long eachMachineId = reservationAssignEachMachine.getEachMachineId();
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isEmpty()) {
            throw new NotFoundException("해당 예약 번호의 예약 내역은 존재하지 않습니다.");
        }
        Reservation reservation = oReservation.get();
        LocalDateTime wantTime = reservation.getWantTime();
        LocalDateTime endTime = reservation.getEndTime();

        // 해당 날짜에 예약 가능한 개별 기계 찾기
        Optional<EachMachine> oEachMachine = eachMachineRepository.findById(eachMachineId);
        if(oEachMachine.isEmpty()) {
            throw new NotFoundException("해당 eachMachine 정보는 존재하지 않습니다.");
        }
        // 개별 기계 reservation에 반영하기
        EachMachine newEachMachine = oEachMachine.get();
        List<EachMachinePossible> newEachMachinePossibleList = eachMachinePossibleRepository.findByEachMachineId(newEachMachine.getId(), wantTime, endTime);

        if(reservation.getEachMachine()==null) {
            reservation.setEachMachine(newEachMachine);
            for(EachMachinePossible eachMachinePossible:newEachMachinePossibleList) {
                eachMachinePossible.setReservePossible(false);
            }
        } else {
            EachMachine oldEachMachine = reservation.getEachMachine();
            List<EachMachinePossible> oldEachMachinePossibleList = eachMachinePossibleRepository.findByEachMachineId(oldEachMachine.getId(), wantTime, endTime);
            for(EachMachinePossible eachMachinePossible:oldEachMachinePossibleList) {
                eachMachinePossible.setReservePossible(true);
            }
            reservation.setEachMachine(newEachMachine);
            for(EachMachinePossible eachMachinePossible:newEachMachinePossibleList) {
                eachMachinePossible.setReservePossible(false);
            }
        }


        return newEachMachine.getId();
    }
}
