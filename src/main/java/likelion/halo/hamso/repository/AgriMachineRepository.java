package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import likelion.halo.hamso.domain.type.Region3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AgriMachineRepository extends JpaRepository<AgriMachine, Long>  {
    Optional<AgriMachine> findByType(AgriMachineType type);
    //Optional<Region>


    @Query("select a from AgriMachine a where a.type =:machineType and a.region.id =:regionId")
    Optional<AgriMachine> findByTypeAndRegion(@Param("machineType") AgriMachineType machineType, @Param("regionId")Long regionId);

    @Query("SELECT m FROM AgriMachine m " +
            "JOIN m.possibleList p " +
            "JOIN m.tagList t " +
            "WHERE m.id = p.machine.id " +
            "AND m.id = t.machine.id " +
            "AND m.region.id = :regionId " +
            "AND (:machineType is null or m.type = :machineType) " +
            "AND (:tagColumn is null or t.tagColumn = :tagColumn) " +
            "AND (:findDate is null or p.findDate = :findDate) " +
            "AND p.reservePossible = true " +
            "AND m.reservePossible = true ")
    List<AgriMachine> search(@Param("regionId") Long regionId,
                                       @Param("machineType") AgriMachineType machineType,
                                       @Param("tagColumn") String tagColumn,
                                       @Param("findDate") LocalDateTime findDate);
}
