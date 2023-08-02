package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.AgriMachineType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "possible")
public class AgriPossible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "possible_id")
    private Long id;

    @Column(name = "origin_cnt")
    private Integer cnt;

    @Column(name = "date")
    @CreationTimestamp
    private LocalDateTime date; // 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private AgriMachine machine;

    @Column(name = "reserve_possible", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean reservePossible; // 예약 가능 여부

}
