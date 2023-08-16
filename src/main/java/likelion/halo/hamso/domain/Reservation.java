package likelion.halo.hamso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.reservation.ReservationInfoDto;
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
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    private LocalDateTime wantTime; // 예약하기 원하는 시간

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // 예약 상태 RESERVING, RESERVED, FINISHED, CANCELED

    @Column(name = "deposit", columnDefinition = "TINYINT(1)")
    @ColumnDefault("0")
    private Boolean deposit; // 입금 여부

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt; // 예약 신청 일자

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agriculture_machine")
    private AgriMachine agriMachine;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;


    @Column(name = "work_type")
    private String workType; // 작업 종류

    @Column(name = "workload")
    private Double workload; // 작업량

    @Column(name = "lent_price")
    private Integer lentPrice; // 임대료

    @OneToOne
    @JoinColumn(name = "each_machine_id")
    private EachMachine eachMachine;

    //==생성 메서드==//
    public static Reservation createReservation(ReservationInfoDto reservationDto, Member member, AgriMachine machine, EachMachine eachMachine) {
        Reservation reservation = new Reservation();
        reservation.setMember(member);
        reservation.setAgriMachine(machine);
        reservation.setWantTime(reservationDto.getWantTime());
        reservation.setStatus(ReservationStatus.RESERVED);
        reservation.setDeposit(false);
        reservation.setWorkType(reservation.getWorkType());
        reservation.setWorkload(reservation.getWorkload());
        reservation.setLentPrice(machine.getPrice() * reservationDto.getReserveDayCnt());
        reservation.setEachMachine(eachMachine);
        return reservation;
    }
}
