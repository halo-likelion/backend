package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import likelion.halo.hamso.domain.type.Region3;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegionInfoDto {
    private Region1 region1; // 특별시, 광역시, 도
    private Region2 region2; // 시, 군, 구
    private Region3 region3; // 임대소

    public RegionInfoDto(AgriRegion agriRegion) {
        this.region1 = agriRegion.getRegion1();
        this.region2 = agriRegion.getRegion2();
        this.region3 = agriRegion.getRegion3();

    }
}
