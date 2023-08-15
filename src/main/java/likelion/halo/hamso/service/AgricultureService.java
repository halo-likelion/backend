package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineUpdateDto;
import likelion.halo.hamso.dto.agriculture.RegionInfoDto;
import likelion.halo.hamso.dto.agriculture.SearchOptionDto;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.repository.AgriMachineRepository;
import likelion.halo.hamso.repository.AgriRegionRepository;
import likelion.halo.hamso.repository.PossibleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AgricultureService {
    private final AgriMachineRepository agriMachineRepository;
    private final AgriRegionRepository agriRegionRepository;
    private final PossibleRepository possibleRepository;

    @Transactional
    public Long addMachine(MachineInfoDto infoDto){
        AgriRegion region = new AgriRegion(infoDto.getRegion1(), infoDto.getRegion2(), infoDto.getRegion3());
        AgriMachine machine = AgriMachine.builder()
                .type(infoDto.getType())
                .content(infoDto.getContent())
                .price(infoDto.getPrice())
                .region(region)
                .build();

        agriMachineRepository.save(machine);
        return machine.getId();
    }

    public MachineInfoDto findByMachineId(Long id){
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(id);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine is not founded with loginId: " + id);
        }
        return new MachineInfoDto(oMachine.get());
    }

    public AgriMachine findByMachineIdReal(Long id){
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(id);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine is not founded with loginId: " + id);
        }
        return oMachine.get();
    }

    public MachineInfoDto findByMachineType(AgriMachineType type){
        Optional<AgriMachine> oMachine = agriMachineRepository.findByType(type);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine not found with type: " + type.toString());
        } else {
            return new MachineInfoDto(oMachine.get());
        }
    }

    public List<MachineInfoDto> findMachineInfoDtoAll() {
        List<AgriMachine> machineList = agriMachineRepository.findAll();
        List<MachineInfoDto> machineDtoList = convertMachineToMachineDto(machineList);
        return machineDtoList;
    }

    public List<RegionInfoDto> findRegionAll() {
        List<AgriRegion> regionList = agriRegionRepository.findAll();
        List<RegionInfoDto> machineDtoList = convertRegionToRegionDto(regionList);
        return machineDtoList;
    }

    @Transactional
    public void updateMachineAll(MachineUpdateDto infoDto) {
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(infoDto.getId());
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine is not founded with loginId: " + infoDto.getId());
        } else {
            AgriMachine machine = oMachine.get();
            machine.setType(infoDto.getType());
            machine.setPrice(infoDto.getPrice());
            machine.setContent(infoDto.getContent());
            machine.setOriCnt(infoDto.getOriCnt());

            Long regionId = infoDto.getRegionId();
            if (regionId != null) {
                AgriRegion region = agriRegionRepository.findById(regionId)
                        .orElseThrow(() -> new NotFoundException("Region not found with id: " + regionId));
                machine.setRegion(region);
            }
            Optional<AgriPossible> possible = possibleRepository.findByMachine(machine);

        if (possible.isPresent()) { // If AgriPossible exists
            AgriPossible agriPossible = possible.get();
            agriPossible.setReservePossible(infoDto.getReservationPossible());
        } else {
            AgriPossible newPossible = AgriPossible.builder()
                    .cnt(0)
                    .findDate(LocalDateTime.now())
                    .machine(machine)
                    .reservePossible(infoDto.getReservationPossible())
                    .build();
            possibleRepository.save(newPossible);
        }
        }
    }

    @Transactional
    public void deleteMachine(Long id) {
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(id);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine is not founded with loginId: " + id);
        } else {
            AgriMachine machine = oMachine.get();
            agriMachineRepository.delete(machine);
        }
    }


    private static List<MachineInfoDto> convertMachineToMachineDto(List<AgriMachine> list) {
        List<MachineInfoDto> dtoList = list.stream()
                .map(a -> new MachineInfoDto(a))
                .collect(Collectors.toList());
        return dtoList;
    }

    private static List<RegionInfoDto> convertRegionToRegionDto(List<AgriRegion> list) {
        List<RegionInfoDto> dtoList = list.stream()
                .map(a -> new RegionInfoDto(a))
                .collect(Collectors.toList());
        return dtoList;
    }

    // 기계 전체 리스트 반환하는 메소드 생성
    public List<AgriMachine> findMachineAll() {
        List<AgriMachine> machineList = agriMachineRepository.findAll();
        return machineList;
    }

    public List<MachineInfoDto> search(SearchOptionDto searchOptionDto) {
        Optional<AgriRegion> oRegion =
                agriRegionRepository.findByEachRegion(
                        searchOptionDto.getRegion1(),
                        searchOptionDto.getRegion2(),
                        searchOptionDto.getRegion3());
        if(oRegion.isEmpty()) {
            throw new NotFoundException("존재하지 않는 지역 정보입니다.");
        }
        log.info("regionId = {}", oRegion.get().getId());

        searchOptionDto.setWantTime(searchOptionDto.getWantTime().plusHours(9));

        log.info("searchOptionDto = {}", searchOptionDto);

        List<AgriMachine> machineList = agriMachineRepository.search(
                oRegion.get().getId(),
                searchOptionDto.getMachineType(),
                searchOptionDto.getTagValue(),
                searchOptionDto.getWantTime());
        return convertMachineToMachineInfoDto(machineList);
    }

    private static List<MachineInfoDto> convertMachineToMachineInfoDto(List<AgriMachine> list) {
        List<MachineInfoDto> dtoList = list.stream()
                .map(a -> new MachineInfoDto(a))
                .collect(Collectors.toList());
        return dtoList;
    }
}
