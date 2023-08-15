package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.EachMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EachMachineRepository extends JpaRepository<EachMachine, Long>  {

}
