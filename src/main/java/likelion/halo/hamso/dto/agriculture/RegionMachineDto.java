package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionMachineDto {
    private Long regionId;
    private Long machineId;

}
