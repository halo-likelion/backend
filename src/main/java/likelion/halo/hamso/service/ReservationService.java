package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineUpdateDto;
import likelion.halo.hamso.dto.agriculture.RegionInfoDto;
import likelion.halo.hamso.exception.MemberDuplicateException;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.repository.AgriMachineRepository;
import likelion.halo.hamso.repository.AgriRegionRepository;
import likelion.halo.hamso.repository.PossibleRepository;
import likelion.halo.hamso.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    public Boolean checkReservePossible(Long machineId, LocalDateTime date){ // 해당 날짜에 해당 농기계 예약 가능여부 반환
        AgriPossible possible = possibleRepository.getMachineDateInfo(machineId, date);
        return possible.getReservePossible();
    }

//    public Boolean checkReservePossible(Long machineId, LocalDateTime date){ // 해당 날짜에 해당 농기계 예약 가능여부 반환
//        List<AgriPossible> possible = possibleRepository.test(machineId);
//        log.info("machineId = {}, date = {}", machineId, date);
//        log.info("possible = {}", possible);
//        return possible.get(0).getReservePossible();
//    }
}
