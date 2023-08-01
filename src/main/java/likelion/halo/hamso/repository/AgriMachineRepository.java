package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.AgriMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgriMachineRepository extends JpaRepository<AgriMachine, Long>  {

}
