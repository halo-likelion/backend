package likelion.halo.hamso.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.alert.MessageDto;
import likelion.halo.hamso.dto.alert.SmsResponseDto;
import likelion.halo.hamso.service.MemberService;
import likelion.halo.hamso.service.ReservationService;
import likelion.halo.hamso.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static java.time.LocalTime.now;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerFunction {
    private final MemberService memberService;
    private final ReservationService reservationService;

    private final SmsService smsService;


    @Scheduled(cron = "0 0 0 * * ?", zone="Asia/Seoul") // 매일 밤 12시 0분에 예약 날짜 마지막날 +1 에 입금이 안되어 있으면 자동으로 예약 취소로 변경
    public void updateReservingToCanceled() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservationList = reservationService.getReservingReservation();
        for(Reservation reservation:reservationList) {
            if(reservation.getWantTime().isBefore(now.plusDays(1))){
                reservationService.updateReservationStatus(reservation.getId(), ReservationStatus.CANCELED);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?", zone="Asia/Seoul") // 매일 밤 12시 0분에 예약 날짜 마지막날에 시간지나고 완료된 예약은 끝남 처리
    public void updateReservedToFinished() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservationList = reservationService.getReservedReservation();
        for(Reservation reservation:reservationList) {
            if(reservation.getWantTime().isBefore(now)){
                reservationService.updateReservationStatus(reservation.getId(), ReservationStatus.FINISHED);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?", zone="Asia/Seoul") // 매일 밤 12시 0분에 예약 전날일 경우 알림 메시지 전송하기
    public void sendReservationNotification() throws UnsupportedEncodingException, URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservationList = reservationService.getReservedReservation();
        for(Reservation reservation:reservationList) {
            LocalDateTime wantTime = reservation.getWantTime();
            int year = wantTime.getYear();
            Month month = wantTime.getMonth();
            int day = wantTime.getDayOfMonth();
            Member member = reservation.getMember();
            MessageDto messageDto = new MessageDto();
            messageDto.setTo(member.getPhoneNo());
            messageDto.setContent("<렛츠-농사> " + member.getName() +"님 "+ reservation.getAgriMachine().getType()+"이(가) [" +year + "년" + month.getValue() + "월" + day + "일" +"]에 "+ reservation.getAgriMachine().getType() +"이(가) 예약상태이십니다.");
            SmsResponseDto response = smsService.sendSms(messageDto);
            log.info("message log = {}", response);
        }
    }



}
