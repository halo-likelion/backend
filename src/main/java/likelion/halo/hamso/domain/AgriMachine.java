package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import lombok.*;

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



}
