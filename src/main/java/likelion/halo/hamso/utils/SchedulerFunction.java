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

//    @Scheduled(cron = "0 * * * * ?") // 매분 확인
    public void resetReservationStatus() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStartDateTime = LocalDateTime.of(now.getYear(), now.getMonth().minus(1), now.getDayOfMonth(), now.getHour(), 0, 0);

        // 현재 시각 한시간 전부터 현재 시각까지의 예약 리스트에서 예약 상태인 것만 가져오기
        List<ReservationsDto> reservationsDtoList = reservationService.getReservationListByDateAndStatus(todayStartDateTime, now, ReservationStatus.RESERVED);
        for(ReservationsDto reservationsDto: reservationsDtoList) {
            if(reservationsDto.getEnd().isBefore(now)) {
                Long reservation_id = reservationsDto.getReservation_id();
                reservationService.setStatus(ReservationStatus.FINISHED, reservation_id);
            }
        }
    }

//    @Scheduled(cron = "0 * * * * ?") // 매분 확인
    public void noShowCheck() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayDate = LocalDateTime.of(now.getYear(), now.getMonth().getValue(), now.getDayOfMonth(), 0, 0, 0);
        LocalDateTime nextDate = LocalDateTime.of(now.getYear(), now.getMonth().getValue(), now.plusDays(1).getDayOfMonth(), 0, 0, 0);

        // 하루동안의 현재 시각까지의 예약 리스트에서 예약이 끝난 상태인 것만 가져오기
        List<ReservationsDto> reservationsDtoList = reservationService.getReservationListByDateAndStatus(todayDate, nextDate, ReservationStatus.FINISHED);
        for(ReservationsDto reservationsDto: reservationsDtoList) {
            if(reservationsDto.getNoShowCheck() == false) { // 예약이 끝난 시간까지 노쇼가 안되어 있을 경우
                memberService.addNoShowCnt(reservationsDto.getMember_sno());
                reservationService.setNoShowStatus(reservationsDto.getReservation_id());
            }
        }
    }

}
