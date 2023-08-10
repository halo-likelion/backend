package likelion.halo.hamso.dto;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.Tag;
import likelion.halo.hamso.domain.type.AgriMachineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationSearchDto {
    private String region1; // 특별시, 광역시, 도
    private String region2; // 시, 군, 구
    private String region3; // 임대소

    private String tagColumn; // 태그

    private AgriMachineType type; // 농기계 종류

    private LocalDateTime wantTime; // 예약하기 원하는 시간

    public ReservationSearchDto(AgriMachine agriMachine, Tag tag, Reservation reservation) {
        this.region1 = agriMachine.getRegion().getRegion1().toString();
        this.region2 = agriMachine.getRegion().getRegion2().toString();
        this.region3 = agriMachine.getRegion().getRegion3().toString();
        this.tagColumn = tag.getTagColumn();
        this.type = agriMachine.getType();
        this.wantTime = reservation.getWantTime();
    }
}
