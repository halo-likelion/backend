package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.type.AgriMachineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgriMachineRepository extends JpaRepository<AgriMachine, Long>  {
    Optional<AgriMachine> findByType(AgriMachineType type);


}
