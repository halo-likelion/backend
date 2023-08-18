package likelion.halo.hamso.dto.reservation;

import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.domain.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationAdminInfoDto {
    private LocalDateTime wantTime; // 예약 원하는 날짜
    private LocalDateTime endTime; // 예약 원하는 날짜
    private LocalDateTime createdAt; // 예약 신청 날짜
    private Long machineId; // / 빌리기 원하는 농기계 ID

    private AgriMachineType machineType; // 빌리기 원하는 농기계 종류 이름
    private Integer price; // 임대료
    private ReservationStatus reservationStatus; // 예약 상태

    private String name; // 예약한 사람 이름
    private String loginId; // 예약한 사람의 아이디
    private String phoneNo; // 예약한 사람의 전화번호

    private String workType; // 작업 종류
    private Double workload; // 작업량

    private Long reservationId;

    public ReservationAdminInfoDto(Reservation reservationInfo) {
        this.wantTime = reservationInfo.getWantTime();
        this.createdAt = reservationInfo.getCreatedAt();
        this.machineId = reservationInfo.getAgriMachine().getId();
        this.machineType = reservationInfo.getAgriMachine().getType();
        this.price = reservationInfo.getAgriMachine().getPrice();
        this.reservationStatus =reservationInfo.getStatus();
        this.name = reservationInfo.getMember().getName();
        this.loginId = reservationInfo.getMember().getLoginId();
        this.phoneNo = reservationInfo.getMember().getPhoneNo();
        this.workload = reservationInfo.getWorkload();
        this.workType = reservationInfo.getWorkType();
        this.reservationId = reservationInfo.getId();
        this.endTime=reservationInfo.getEndTime();
    }
}
