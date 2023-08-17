package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.EachMachinePossible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EachMachinePossibleRepository extends JpaRepository<EachMachinePossible, Long> {

    @Query("select p from EachMachinePossible p where p.id=:eachMachineId")
    List<EachMachinePossible> findListById(Long eachMachineId);

    @Query("select p from EachMachinePossible p " +
            "where p.eachMachine.machine.id=:machineId " +
            "and p.reservePossible=true " +
            "and :wantTime <= p.findDate and p.findDate <= :endTime " +
            "group by p.eachMachine.id")
    List<EachMachinePossible> findByEachMachineIdAndDate(Long machineId, LocalDateTime wantTime, LocalDateTime endTime);

    @Query("select p from EachMachinePossible p " +
            "where p.eachMachine.id=:eachMachineId " +
            "and :wantTime <= p.findDate and p.findDate <= :endTime")
    List<EachMachinePossible> findByEachMachineId(Long eachMachineId, LocalDateTime wantTime, LocalDateTime endTime);
}
