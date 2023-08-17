package likelion.halo.hamso.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.agriculture.MachineInfoDto;
import likelion.halo.hamso.dto.alert.MessageDto;
import likelion.halo.hamso.dto.alert.SmsResponseDto;
import likelion.halo.hamso.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
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

//@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerFunction {
    private final MemberService memberService;
    private final ReservationService reservationService;
    private final PossibleService possibleService;
    private final SmsService smsService;
    private final AgricultureService agricultureService;


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
            messageDto.setContent("<렛츠-농사> " + member.getName() +"님 "+ reservation.getAgriMachine().getType()+"이(가) [" +year + "년" + month.getValue() + "월" + day + "일" +"]에 "+ reservation.getAgriMachine().getType() +"이(가) 예약상태 예정입니다.");
            SmsResponseDto response = smsService.sendSms(messageDto);
            log.info("message log = {}", response);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?", zone="Asia/Seoul") // 매달 1일
    public void insertMonthNewPossibleData(){
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        Month month = now.getMonth();
        int lastDay = month.maxLength();
        LocalDateTime time = LocalDateTime.of(year, month, 1, 0, 0, 0);
        List<AgriMachine> machineList = agricultureService.findMachineAll();

        // 이전 달 정보 전부 삭제
        possibleService.deleteAll();

        // 기계 전체 가져와서 각각 매달 데이터 값 넣기
        for(AgriMachine machine:machineList) {
            for(int i = 0;i<=lastDay;i++) {
                AgriPossible possible = AgriPossible.builder()
                        .cnt(machine.getOriCnt())
                        .findDate(time.plusDays(i))
                        .machine(machine)
                        .reservePossible(true)
                        .build();
                possibleService.addPossible(possible);
            }
        }

    }


}
