package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import likelion.halo.hamso.domain.type.Region3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgriRegionRepository extends JpaRepository<AgriRegion, Long>  {

    @Query("select r from AgriRegion r where r.region1=:region1 and r.region2=:region2 " +
            "and (:region3 is null or r.region3=:region3)")
    Optional<AgriRegion> findByEachRegion(Region1 region1, Region2 region2, Region3 region3);
}
