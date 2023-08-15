package likelion.halo.hamso.dto.agriculture;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.domain.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineUpdateDto {
    private Long id;
    private AgriMachineType type; // 농기계 종류
    private Integer price; // 임대 가격
    private String content; // 농기계 설명
    private AgriRegion region;
    private Long regionId; // 지역ID
    private AgriPossible possible;
    private Boolean reservationPossible; // 예약 가능 여부 (예약 가능, 예약 불가)
    private Integer oriCnt; // 농기계 총 개수

    public MachineUpdateDto(AgriMachine agriMachine, AgriPossible agriPossible) {
        this.id = agriMachine.getId();
        this.type = agriMachine.getType();
        this.price = agriMachine.getPrice();
        this.content = agriMachine.getContent();
        this.regionId = agriMachine.getRegion().getId();
        this.reservationPossible = agriPossible.getReservePossible();
        this.oriCnt = agriMachine.getOriCnt();
    }
}
