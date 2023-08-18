package likelion.halo.hamso.controller;

import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.dto.agriculture.*;
import likelion.halo.hamso.repository.PossibleRepository;
import likelion.halo.hamso.service.AgricultureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agriculture")
public class AgricultureController {
    private final AgricultureService agricultureService;
    private final PossibleRepository possibleRepository;

    @GetMapping("/machine-list")
    public ResponseEntity<List<MachineInfoDto>> getMachineList() {
        List<MachineInfoDto> machines = agricultureService.findMachineInfoDtoAll();
        return new ResponseEntity<>(machines, HttpStatus.OK);
    }
    @GetMapping("/region-list")
    public ResponseEntity<List<RegionInfoDto>> getRegionList() {
        List<RegionInfoDto> regions = agricultureService.findRegionAll();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineInfoDto> getMachineById(@PathVariable("id") Long id) {
        MachineInfoDto machine = agricultureService.findByMachineId(id);
        return new ResponseEntity<>(machine, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteMachineById(@PathVariable("id") Long id) {
        agricultureService.deleteMachine(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update-all")
    public ResponseEntity<Void> updateMachineAll(@RequestBody MachineUpdateDto machineInfo) {
        agricultureService.updateMachineAll(machineInfo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @PostMapping("/update-machine-status")
//    public ResponseEntity<Void> updateMachineRevStatus(@RequestBody MachineStatusUpdateDto info) {
//        agricultureService.updateMachineRevStatus(info);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PostMapping("/search")
    public ResponseEntity<List<MachineInfoDto>> getSearchResult(@RequestBody SearchOptionDto searchOptionDto) {
        log.info("SearchOptionDto Controller = {}", searchOptionDto);
        List<MachineInfoDto> searchResult = agricultureService.search(searchOptionDto);
        return new ResponseEntity<>(searchResult, HttpStatus.OK);

    }

    @PostMapping("/insert/machine")
    public ResponseEntity<Long> addMachine(@RequestBody MachineInsertDto machineInsertDto) {
        Long machineId = agricultureService.addMachine(machineInsertDto);
        return new ResponseEntity<>(machineId, HttpStatus.OK);
    }

    @DeleteMapping("/delete/machine")
    public ResponseEntity<Void> deleteMachine(@RequestParam("machineId") Long machineId) {
        agricultureService.deleteMachine(machineId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/insert/tag")
    public ResponseEntity<Long> addTag(@RequestBody TagDto tagDto) {
        Long tagId = agricultureService.addTag(tagDto);
        return new ResponseEntity<>(tagId, HttpStatus.OK);
    }

    @DeleteMapping("/delete/tag")
    public ResponseEntity<Void> deleteTag(@RequestParam("tagId") Long tagId) {
        agricultureService.deleteTag(tagId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/possible")
    public List<AgriPossible> getPossibleList() {
        return possibleRepository.findByMachineId(103L);
    }

    @PostMapping("/update/image")
    public void updateMachineImageByType(@RequestBody MachineImageUpdateDto machineImageUpdateDto) {
        agricultureService.updateMachineImageByType(machineImageUpdateDto);
    }
}
