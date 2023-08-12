package likelion.halo.hamso.utils;

import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.service.MemberService;
import likelion.halo.hamso.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalTime.now;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerFunction {
    private final MemberService memberService;
    private final ReservationService reservationService;



    @Scheduled(cron = "0 0 0 * * ?", zone="Asia/Seoul") // 매일 밤 12시 0분에 예약 날짜 마지막날 +1 에 입금이 안되어 있으면 자동으로 예약 취소로 변경
    public void updateReservingToCanceled() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservationList = reservationService.getReservingReservation();
        for(Reservation reservation:reservationList) {
            if(reservation.getWantTime().isAfter(now.plusDays(1)) && reservation.getStatus() == ReservationStatus.RESERVING){
                reservationService.updateReservationStatus(reservation.getId(), ReservationStatus.CANCELED);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?", zone="Asia/Seoul") // 매일 밤 12시 0분에 예약 날짜 마지막날에 시간지나고 완료된 예약은 끝남 처리
    public void updateReservedToFinished() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservationList = reservationService.getReservingReservation();
        for(Reservation reservation:reservationList) {
            if(reservation.getWantTime().isAfter(now) && reservation.getStatus() == ReservationStatus.RESERVED){
                reservationService.updateReservationStatus(reservation.getId(), ReservationStatus.FINISHED);
            }
        }
    }



}
