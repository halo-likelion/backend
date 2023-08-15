package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.type.AgriMachineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgriMachineRepository extends JpaRepository<AgriMachine, Long>  {
    Optional<AgriMachine> findByType(AgriMachineType type);
    //Optional<Region>


    @Query("select a from AgriMachine a where a.type =:machineType and a.region.id =:regionId")
    Optional<AgriMachine> findByTypeAndRegion(@Param("machineType") AgriMachineType machineType, @Param("regionId")Long regionId);

}
