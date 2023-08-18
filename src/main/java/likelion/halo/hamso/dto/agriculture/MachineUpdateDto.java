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
public class MachineUpdateDto {
    private Long id;
    private AgriMachineType type; // 농기계 종류

    private Integer price; // 임대 가격

    private String content; // 농기계 설명

    private AgriRegion region;

    private String imageUrl; // 농기계 설명

    public MachineUpdateDto(AgriMachine agriMachine) {
        this.id = agriMachine.getId();
        this.type = agriMachine.getType();
        this.price = agriMachine.getPrice();
        this.content = agriMachine.getContent();
        this.region = agriMachine.getRegion();
        this.imageUrl = agriMachine.getImageUrl();
    }
}
