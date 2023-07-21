package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>  {
    Member findByLoginId(String loginId);
}
