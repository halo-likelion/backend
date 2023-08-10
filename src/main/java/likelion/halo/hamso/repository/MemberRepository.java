package likelion.halo.hamso.repository;

import likelion.halo.hamso.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>  {
    Optional<Member> findByLoginId(String loginId);

    @Query("select count(m) from Member m where m.phoneNo=:phoneNo")
    int findByPhoneNo(@Param("phoneNo")String phoneNo);
}
