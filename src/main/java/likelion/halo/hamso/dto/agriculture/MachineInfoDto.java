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
public class MachineInfoDto {
    private AgriMachineType type; // 농기계 종류

    private Integer price; // 임대 가격

    private String content; // 농기계 설명

    private Boolean reservePossible; // 예약 가능 여부

    private String region1;
    private String region2;

    public MachineInfoDto(AgriMachine agriMachine) {
        this.type = agriMachine.getType();
        this.price = agriMachine.getPrice();
        this.content = agriMachine.getContent();
        this.region1 = agriMachine.getRegion().getRegion1().toString();
        this.region2 = agriMachine.getRegion().getRegion2().toString();
    }
}
