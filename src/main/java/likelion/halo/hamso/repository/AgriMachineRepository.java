package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.type.AgriMachineType;
//import likelion.halo.hamso.
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

    @Query("select m from AgriMachine m where m.region.id=:id")
    List<AgriMachine> findByRegionId(@Param("id") Long id);

    @Query("select DISTINCT m from AgriMachine m " +
            "LEFT JOIN m.tag t " +
            "LEFT JOIN m.reservation r " +
            "WHERE (:region1 IS NULL OR m.region.region1 = :region1) " +
            "AND (:region2 IS NULL OR m.region.region2 = :region2) " +
            "AND (:region3 IS NULL OR m.region.region3 = :region3) " +
            "AND (:tagColumn IS NULL OR t.tagColumn = :tagColumn) " +
            "AND (:type IS NULL OR m.type = :type) " +
            "AND (:wantTime IS NULL OR :wantTime IN (SELECT res.wantTime FROM r) = :wantTime)"
    )
    List<AgriMachine> findBySearch(String region1, String region2, String region3, String tagColumn, AgriMachineType type, LocalDateTime wantTime);
    //List<AgriMachine> findByRegionId(Long regionId);
}
