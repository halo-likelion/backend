package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "each_machine_possible")
public class EachMachinePossible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "each_machine_possible_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "each_machine_id")
    private EachMachine eachMachine;

    @Column(name = "reserve_possible", columnDefinition = "TINYINT(1)")
    @ColumnDefault("1")
    private Boolean reservePossible; // 예약 가능 여부

    private LocalDateTime findDate; // 날짜

}
