package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.EachMachine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EachMachineInfoDto {
    private Boolean eachMachinePossible; // 예약 가능 여부
    private Long machineId;
    private Long eachMachineId;

    public EachMachineInfoDto(EachMachine eachMachineInfo) {
        this.eachMachineId = eachMachineInfo.getId();
        this.eachMachinePossible = eachMachineInfo.getEachMachinePossible();
        this.machineId = eachMachineInfo.getMachine().getId();
    }
}
