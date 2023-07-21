package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id")
    private String loginId; // 로그인 아이디

    @Column(name = "pwd")
    private String password; // 비밀번호

    @Column(name = "name")
    private String name; // 회원의 이름

    @Column(name = "phn")
    private String phoneNo; // 전화번호

    @Column(name="email")
    private String email; // 이메일

}
