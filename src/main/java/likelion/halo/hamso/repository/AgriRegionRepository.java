package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.AgriRegion;
import likelion.halo.hamso.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository
public interface AgriRegionRepository extends JpaRepository<AgriRegion, Long>  {

    List<AgriRegion> findByRegion(String region);
}
