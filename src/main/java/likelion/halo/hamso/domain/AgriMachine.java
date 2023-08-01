package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.AgriMachineType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "agriculture_machine")
public class AgriMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agriculture_machine_id")
    private Long id;

    @Column(name = "agriculture_machine_name")
    private String name; // 농기계 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "agriculture_machine_type")
    private AgriMachineType type; // 농기계 종류

    @Column(name = "agriculture_machine_price")
    private Integer price; // 임대 가격

    @Column(name = "agriculture_machine_content")
    private String content; // 농기계 설명

    @Column(name = "reserve_possible", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean reservePossible; // 예약 가능 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agriculture_machine_id")
    private AgriRegion region;

}
