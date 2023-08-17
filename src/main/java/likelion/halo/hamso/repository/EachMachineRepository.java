package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriPossible;
import likelion.halo.hamso.domain.EachMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EachMachineRepository extends JpaRepository<EachMachine, Long>  {
    @Query("select e from EachMachine e " +
            "where " +
            "e.eachMachinePossible=true " +
            "and e.machine.id=:machineId " +
            "order by e.id")
    List<EachMachine> findAllByMachinePossible(Long machineId);
}
