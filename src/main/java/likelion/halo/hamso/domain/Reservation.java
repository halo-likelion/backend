package likelion.halo.hamso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.ReservationStatus;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private LocalDateTime wantTime; // 예약하기 원하는 시간

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // 예약 상태 RESERVED, FINISHED, CANCELED

    @Column(name = "deposit", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean deposit; // 입금 여부

    @Column(name = "created_at")
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now(); // 예약 신청 일자

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agriculture_machine")
    private AgriMachine agriMachine;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;
}
