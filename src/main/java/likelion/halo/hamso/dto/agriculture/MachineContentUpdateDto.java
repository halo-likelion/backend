package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.type.AgriMachineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineContentUpdateDto {
    private AgriMachineType machineType;
    private String content;
}
