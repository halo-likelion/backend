package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import likelion.halo.hamso.domain.type.Region3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchOptionDto {
    private Region1 region1;
    private Region2 region2;
    private Region3 region3;
    private String tagValue;
    private AgriMachineType machineType;
    private LocalDateTime wantTime;
}
