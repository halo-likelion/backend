package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineStatusUpdateDto;
import likelion.halo.hamso.dto.agriculture.MachineUpdateDto;
import likelion.halo.hamso.dto.agriculture.RegionInfoDto;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.repository.AgriMachineRepository;
import likelion.halo.hamso.repository.AgriRegionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Long addMachine(MachineInfoDto infoDto){
        AgriRegion region = new AgriRegion(infoDto.getRegion1(), infoDto.getRegion2(), infoDto.getRegion3());
        AgriMachine machine = AgriMachine.builder()
                .type(infoDto.getType())
                .content(infoDto.getContent())
                .price(infoDto.getPrice())
                .region(region)
                .reservePossible(infoDto.getReservePossible())
                .build();

        agriMachineRepository.save(machine);
        return machine.getId();
    }

    public MachineInfoDto findById(Long id){
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(id);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine is not founded with loginId: " + id);
        }
        return new MachineInfoDto(oMachine.get());
    }

    public MachineInfoDto findByMachineType(AgriMachineType type){
        Optional<AgriMachine> oMachine = agriMachineRepository.findByType(type);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine not found with type: " + type.toString());
        } else {
            return new MachineInfoDto(oMachine.get());
        }
    }

    public List<MachineInfoDto> findMachineAll() {
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
            machine.setContent(infoDto.getContent());
            machine.setRegion(infoDto.getRegion());
            machine.setPrice(infoDto.getPrice());
        }
    }

    @Transactional
    public void updateMachineRevStatus(MachineStatusUpdateDto infoDto) {
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(infoDto.getId());
        if(oMachine.isEmpty()) {
            throw new NotFoundException("Machine is not founded with loginId: " + infoDto.getId());
        } else {
            AgriMachine machine = oMachine.get();
            machine.setReservePossible(infoDto.getReservePossible());
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
}
