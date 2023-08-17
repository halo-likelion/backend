package likelion.halo.hamso.dto.each;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindEachMachineDto {
    Long machineId;
    LocalDateTime wantTime;
    LocalDateTime endTime;
}
