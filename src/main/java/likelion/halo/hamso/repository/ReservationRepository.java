package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import likelion.halo.hamso.domain.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 전체 예약을 최신순으로 반환 (예약중, 완료, 삭제 포함)
    @Query("select r from Reservation r where r.member.loginId = :loginId order by r.wantTime DESC ")
    List<Reservation> findAllByLoginIdRecent(@Param("loginId") String loginId);


    // 해당 시간대에 예약 여부 (건수) 반환 1이면 예약이 이미 있는 상황
    @Query("select count(r) from Reservation r where r.wantTime = :wantTime and r.status = 'RESERVED'" +
            " and r.agriMachine.id = :machineId ")
    int isReserved(@Param("wantTime")LocalDateTime wantTime,@Param("machineId")Long machineId);


    // 해당 로그인 아이디의 회원 예약 내역 리스트 반환
    @Query("select r from Reservation r where r.member.loginId = :loginId and r.status =:status")
    List<Reservation> getStatusList(@Param("loginId") String loginId, @Param("status") ReservationStatus status);

    // 오늘 전체 예약 리스트
    @Query("select r from Reservation r where r.wantTime >= :wantTime ")
    List<Reservation> getTodayReserveList(@Param("wantTime")LocalDateTime wantTime);

    // 오늘 해당 지역 전체 예약 리스트
    @Query("select r from Reservation r where r.wantTime >= :wantTime and r.agriMachine.region.region1 =:region1 and r.agriMachine.region.region2 =:region2")
    List<Reservation> getTodayReserveListRegion(@Param("wantTime")LocalDateTime wantTime,
                                                @Param("region1")Region1 region1,
                                                @Param("region2")Region2 region2);

    // 해당 로그인아이디 회원의 예약 상태에 따른 예약 내역 리스트 반환
    @Query("select r from Reservation r where r.member.loginId = :loginId and r.status = :status")
    List<Reservation> getReservationListBySnoAndStatus(@Param("studentNo") String studentNo, @Param("status") ReservationStatus status);

}
