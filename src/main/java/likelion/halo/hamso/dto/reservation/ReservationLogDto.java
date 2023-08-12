package likelion.halo.hamso.dto.reservation;

import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLogDto {
    private LocalDateTime wantTime; // 예약 원하는 날짜
    private LocalDateTime createdAt; // 예약 신청 날짜
    private Long machineId; // 빌리기 원하는 농기계 아이디
    private Integer price; // 임대료
    private ReservationStatus reservationStatus; // 예약 상태
    private Long reservationId;

    private String name; // 예약한 사람 이름

    public ReservationLogDto(Reservation reservationInfo) {
        this.reservationId = reservationInfo.getId();
        this.wantTime = reservationInfo.getWantTime();
        this.createdAt = reservationInfo.getCreatedAt();
        this.machineId = reservationInfo.getAgriMachine().getId();
        this.price = reservationInfo.getLentPrice();
        this.reservationStatus =reservationInfo.getStatus();
        this.name = reservationInfo.getMember().getName();
    }
}
