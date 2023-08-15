package likelion.halo.hamso.dto.agriculture;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import likelion.halo.hamso.domain.AgriMachine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EachMachineDto {
    private Boolean eachMachinePossible; // 예약 가능 여부
    private Long machineId;
}
