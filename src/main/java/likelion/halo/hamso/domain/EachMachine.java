package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.dto.agriculture.EachMachineDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "each_machine")
public class EachMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "each_machine_id")
    private Long id;

    @Column(name = "each_machine_possible", columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private Boolean eachMachinePossible; // 예약 가능 여부

    @Column(name = "reserve_status", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean reserveStatus; // 예약 여부

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private AgriMachine machine;
}
