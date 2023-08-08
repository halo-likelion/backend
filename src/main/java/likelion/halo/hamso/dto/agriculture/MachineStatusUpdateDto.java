package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriPossible;
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

    public MachineStatusUpdateDto(AgriPossible possible) {
        this.id = possible.getMachine().getId();
        this.reservePossible = possible.getReservePossible();
    }
}
