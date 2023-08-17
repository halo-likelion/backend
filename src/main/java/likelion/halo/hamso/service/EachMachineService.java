package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.*;
import likelion.halo.hamso.dto.agriculture.EachMachineDto;
import likelion.halo.hamso.dto.agriculture.EachMachineInfoDto;
import likelion.halo.hamso.dto.each.FindEachMachineDto;
import likelion.halo.hamso.exception.NotEnoughCntException;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EachMachineService {
    private final AgriMachineRepository agriMachineRepository;
    private final AgriRegionRepository agriRegionRepository;
    private final ReservationRepository reservationRepository;
    private final PossibleRepository possibleRepository;
    private final MemberRepository memberRepository;
    private final EachMachineRepository eachMachineRepository;
    private final EachMachinePossibleRepository eachMachinePossibleRepository;


    @Transactional
    public Long insertEachMachine(EachMachineDto eachMachineDto) {
        Long machineId = eachMachineDto.getMachineId();
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(machineId);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("해당 기계의 정보는 존재하지 않습니다!");
        }
        AgriMachine machine = oMachine.get();
        EachMachine eachMachine = new EachMachine();
        eachMachine.setMachine(machine);
        eachMachine.setEachMachinePossible(eachMachineDto.getEachMachinePossible());
        eachMachineRepository.save(eachMachine);
        addCnt(machineId);


        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        Month month = now.getMonth();
        int lastDay = month.maxLength();
        LocalDateTime time = LocalDateTime.of(year, month, 1, 0, 0, 0);
        log.info("time = {}", time);
        for(int i = 0;i<=lastDay;i++) {
            EachMachinePossible possible = EachMachinePossible.builder()
                    .findDate(time.plusDays(i))
                    .eachMachine(eachMachine)
                    .reservePossible(true)
                    .build();
            eachMachinePossibleRepository.save(possible);
        }
        return eachMachine.getId();
    }

    @Transactional
    public void deleteEachMachine(Long eachMachineId) {
        Optional<EachMachine> oEachMachine = eachMachineRepository.findById(eachMachineId);
        if(oEachMachine.isEmpty()) {
            throw new NotFoundException("해당 기계의 정보는 존재하지 않습니다!");
        }
        EachMachine eachMachine = oEachMachine.get();
        eachMachineRepository.delete(eachMachine);
        removeCnt(eachMachine.getMachine().getId());
        List<EachMachinePossible> eachMachinePossibles = eachMachinePossibleRepository.findListById(eachMachineId);
        for(EachMachinePossible eachMachinePossible:eachMachinePossibles) {
            eachMachinePossibleRepository.delete(eachMachinePossible);
        }
    }

    @Transactional
    public Integer removeCnt(Long machineId) {
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(machineId);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("해당 기계의 정보는 존재하지 않습니다!");
        }
        AgriMachine machine = oMachine.get();
        int cnt = machine.getOriCnt();
        cnt--;
        if (cnt < 0) {
            throw new NotEnoughCntException("더 이상의 삭제는 불가능합니다.");
        }
        List<AgriPossible> agriPossibles = possibleRepository.findByMachineId(machineId);
        if (cnt <= 0) {
            for(AgriPossible agriPossible:agriPossibles) {
                if(agriPossible.getReservePossible()) {
                    agriPossible.setReservePossible(false);
                }
            }
            return 0;
        }
        for(AgriPossible agriPossible:agriPossibles) {
            agriPossible.setCnt(cnt);
        }
        machine.setOriCnt(cnt);
        return machine.getOriCnt();
    }

    @Transactional
    public Integer addCnt(Long machineId) {
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(machineId);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("해당 기계의 정보는 존재하지 않습니다!");
        }
        AgriMachine machine = oMachine.get();
        int cnt = machine.getOriCnt();
        cnt++;

        List<AgriPossible> agriPossibles = possibleRepository.findByMachineId(machineId);
        if (cnt-1 == 0) {
            for(AgriPossible agriPossible:agriPossibles) {
                if(!agriPossible.getReservePossible()){
                    agriPossible.setReservePossible(true);
                }
            }
        }
        for(AgriPossible agriPossible:agriPossibles) {
            agriPossible.setCnt(cnt);
        }
        machine.setOriCnt(cnt);
        return machine.getOriCnt();
    }

    @Transactional
    public Boolean updateMachinePossible(Long eachMachineId) {
        Optional<EachMachine> oEachMachine = eachMachineRepository.findById(eachMachineId);
        if(oEachMachine.isEmpty()) {
            throw new NotFoundException("해당 기계의 정보는 존재하지 않습니다!");
        }
        EachMachine eachMachine = oEachMachine.get();
        Long machineId = eachMachine.getMachine().getId();
        if(eachMachine.getEachMachinePossible()) {
            eachMachine.setEachMachinePossible(false);
            removeCnt(machineId);
        } else {
            eachMachine.setEachMachinePossible(true);
            addCnt(machineId);
        }
        return eachMachine.getEachMachinePossible();
    }

//    public List<EachMachine> getEachMachinePossibleList(Long machineId) {
//        List<EachMachine> eachMachineList = eachMachineRepository.findAllByMachinePossible(machineId);
//        return eachMachineList;
//    }

    public List<EachMachineInfoDto> getEachMachinePossibleDtoList(FindEachMachineDto findEachMachineDto) {
        List<EachMachinePossible> eachMachinePossibleList = eachMachinePossibleRepository.findByEachMachineIdAndDate(findEachMachineDto.getMachineId(), findEachMachineDto.getWantTime(), findEachMachineDto.getEndTime());

        List<EachMachineInfoDto> eachMachineInfoDtoList = convertEachMachinePossibleToEachMachineInfoDto(eachMachinePossibleList);
        return eachMachineInfoDtoList;
    }

    private static List<EachMachineInfoDto> convertEachMachinePossibleToEachMachineInfoDto(List<EachMachinePossible> reservationList) {
        List<EachMachineInfoDto> reservationLogDtoList = reservationList.stream()
                .map(a -> new EachMachineInfoDto(a.getEachMachine()))
                .collect(Collectors.toList());
        return reservationLogDtoList;
    }

    public EachMachine findById(Long eachMachineId) {
        Optional<EachMachine> oEachMachine = eachMachineRepository.findById(eachMachineId);
        if(oEachMachine.isEmpty()) {
            throw new NotFoundException("해당 기계의 정보는 존재하지 않습니다!");
        }
        return oEachMachine.get();
    }
}
