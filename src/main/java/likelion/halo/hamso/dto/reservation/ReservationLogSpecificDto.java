package likelion.halo.hamso.dto.reservation;

import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationLogSpecificDto {
    private LocalDateTime wantTime; // 예약 원하는 날짜
    private LocalDateTime endTime; // 예약 원하는 날짜
    private Integer reserveDayCnt;
    private LocalDateTime createdAt; // 예약 신청 날짜
    private Long machineId; // 빌리기 원하는 농기계 아이디
    private Integer price; // 임대료
    private ReservationStatus reservationStatus; // 예약 상태

    private Boolean deposit; // 입금 여부

    private AgriRegion region; // 예약한 곳의 지역 정보

    private Long reservationId; // 예약 번호

    private String name; // 예약한 사람 이름

    public ReservationLogSpecificDto(Reservation reservationInfo) {
        this.wantTime = reservationInfo.getWantTime();
        this.createdAt = reservationInfo.getCreatedAt();
        this.machineId = reservationInfo.getAgriMachine().getId();
        this.price = reservationInfo.getLentPrice();
        this.reservationStatus =reservationInfo.getStatus();
        this.deposit = reservationInfo.getDeposit();
        this.region =  reservationInfo.getAgriMachine().getRegion();
        this.reservationId = reservationInfo.getId();
        this.name = reservationInfo.getMember().getName();
        this.endTime = reservationInfo.getEndTime();
        this.reserveDayCnt = reservationInfo.getReserveDayCnt();
    }
}
