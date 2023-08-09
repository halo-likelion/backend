package likelion.halo.hamso.controller;

import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.agriculture.MachineStatusUpdateDto;
import likelion.halo.hamso.dto.agriculture.MachineUpdateDto;
import likelion.halo.hamso.dto.agriculture.RegionInfoDto;
import likelion.halo.hamso.service.AgricultureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agriculture")
public class AgricultureController {
    private final AgricultureService agricultureService;

    @GetMapping("/machine-list")
    public ResponseEntity<List<MachineInfoDto>> getMachineList() {
        List<MachineInfoDto> machines = agricultureService.findMachineAll();
        return new ResponseEntity<>(machines, HttpStatus.OK);
    }
    @GetMapping("/region-list")
    public ResponseEntity<List<RegionInfoDto>> getRegionList() {
        List<RegionInfoDto> regions = agricultureService.findRegionAll();
        return new ResponseEntity<>(regions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MachineInfoDto> getMachineById(@PathVariable("id") Long id) {
        MachineInfoDto machine = agricultureService.findById(id);
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

    @PostMapping("/update-machine-status")
    public ResponseEntity<Void> updateMachineRevStatus(@RequestBody MachineStatusUpdateDto info) {
        agricultureService.updateMachineRevStatus(info);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/machies_region/{regionId}")
<<<<<<< HEAD
    public ResponseEntity<List<MachineInfoDto>> getMachinesByRegion(@PathVariable("regionId") Long regionId) {
        //로그 추가
        System.out.println("Received regionId: " + regionId);

        List<MachineInfoDto> machines = agricultureService.findByRegionId(regionId);
=======
    public ResponseEntity<List<MachineInfoDto>> getMachinesByRegion(@PathVariable("region") Long regionId) {
        List<MachineInfoDto> machines = agricultureService.findMachinesByRegionId(regionId);
>>>>>>> 4f963bfb2992c772ea3df805b8410603453d2810
        return new ResponseEntity<>(machines, HttpStatus.OK);
    }
}
