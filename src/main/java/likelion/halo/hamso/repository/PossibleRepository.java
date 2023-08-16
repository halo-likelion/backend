package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.Reservation;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import likelion.halo.hamso.domain.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Date;
import java.util.List;

@Repository
public interface PossibleRepository extends JpaRepository<AgriPossible, Long> {

    // 해당 날짜와 농기계 아이디에 해당하는 농기계 정보 반환
    @Query("select p from AgriPossible p where p.machine.id = :machineId and p.findDate = :findDate")
    AgriPossible getMachineDateInfo(@Param("machineId") Long machineId, @Param("findDate") LocalDateTime findDate);


    @Query("select p from AgriPossible p where p.machine.id =:machineId and p.findDate >=:start and p.findDate <=:end")
    List<AgriPossible> getPossibleList(Long machineId, LocalDateTime start, LocalDateTime end);

    @Query("select p from AgriPossible p where p.machine.id =:machineId")
    List<AgriPossible> findByMachineId(Long machineId);
}
