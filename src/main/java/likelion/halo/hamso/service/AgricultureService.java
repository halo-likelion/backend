package likelion.halo.hamso.service;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.Tag;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.dto.agriculture.*;
import likelion.halo.hamso.exception.NotFoundException;
import likelion.halo.hamso.repository.AgriMachineRepository;
import likelion.halo.hamso.repository.AgriRegionRepository;
import likelion.halo.hamso.repository.PossibleRepository;
import likelion.halo.hamso.repository.TagRepository;
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
public class AgricultureService {
    private final AgriMachineRepository agriMachineRepository;
    private final AgriRegionRepository agriRegionRepository;
    private final PossibleRepository possibleRepository;

    private final TagRepository tagRepository;

    @Transactional
    public Long addMachine(MachineInsertDto infoDto){
        Optional<AgriRegion> oRegion = agriRegionRepository.findByEachRegion(infoDto.getRegion1(), infoDto.getRegion2(), infoDto.getRegion3());
        if(oRegion.isEmpty()) {
            throw new NotFoundException("해당 지역 정보는 존재하지 않습니다.");
        }

        AgriMachine machine = AgriMachine.builder()
                .type(infoDto.getType())
                .content(infoDto.getContent())
                .price(infoDto.getPrice())
                .region(oRegion.get())
                .oriCnt(infoDto.getOriCnt())
                .reservePossible(infoDto.getReservePossible())
                .build();

        agriMachineRepository.save(machine);

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        Month month = now.getMonth();
        int lastDay = month.maxLength();
        LocalDateTime time = LocalDateTime.of(year, month, 1, 0, 0, 0);
        log.info("time = {}", time);
        for(int i = 0;i<=lastDay;i++) {
            AgriPossible possible = AgriPossible.builder()
                    .cnt(machine.getOriCnt())
                    .findDate(time.plusDays(i))
                    .machine(machine)
                    .reservePossible(machine.getReservePossible())
                    .build();
            possibleRepository.save(possible);
        }
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
            machine.setContent(infoDto.getContent());
            machine.setRegion(infoDto.getRegion());
            machine.setPrice(infoDto.getPrice());
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

        searchOptionDto.setWantTime(searchOptionDto.getWantTime());

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

    public List<AdminMachineInfoDto> getAdminMachineInfoList(Long regionId) {
        List<AgriMachine> agriMachineList = agriMachineRepository.findByRegionId(regionId);
        List<AdminMachineInfoDto> dtoList = convertMachineToAdminMachineInfoDto(agriMachineList);
        return dtoList;
    }

    private static List<AdminMachineInfoDto> convertMachineToAdminMachineInfoDto(List<AgriMachine> list) {
        List<AdminMachineInfoDto> dtoList = list.stream()
                .map(a -> new AdminMachineInfoDto(a))
                .collect(Collectors.toList());
        return dtoList;
    }

    @Transactional
    public Long addTag(TagDto tagDto) {
        Long machineId = tagDto.getMachineId();
        Optional<AgriMachine> oMachine = agriMachineRepository.findById(machineId);
        if(oMachine.isEmpty()) {
            throw new NotFoundException("해당 정보의 기계는 존재하지 않습니다!");
        }
        Tag tag = Tag.builder()
                .machine(oMachine.get())
                .tagColumn(tagDto.getTagValue())
                .build();
        tagRepository.save(tag);
        return tag.getId();
    }

    @Transactional
    public void deleteTag(Long tagId) {
        Optional<Tag> oTag = tagRepository.findById(tagId);
        if(oTag.isEmpty()) {
            throw new NotFoundException("해당 정보의 태그는 존재하지 않습니다!");
        }
        tagRepository.delete(oTag.get());
    }
}
