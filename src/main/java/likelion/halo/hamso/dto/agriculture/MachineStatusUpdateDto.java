package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.type.AgriMachineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineStatusUpdateDto {
    private Long id;

    private Boolean reservePossible;

    public MachineStatusUpdateDto(AgriMachine agriMachine) {
        this.id = agriMachine.getId();
        this.reservePossible = agriMachine.getReservePossible();
    }
}
