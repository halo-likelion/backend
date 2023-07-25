package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import likelion.halo.hamso.dto.Member.MemberDto;
import likelion.halo.hamso.dto.Member.MemberJoinDto;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter @ToString
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "login_id", unique = true)
    private String loginId; // 로그인 아이디

    @Column(name = "pwd")
    private String password; // 비밀번호

    @Column(name = "name")
    private String name; // 회원의 이름

    @Column(name = "phn")
    private String phoneNo; // 전화번호

    @Column(name="email")
    private String email; // 이메일

    public Member(MemberJoinDto memberInfo) {
        this.loginId = memberInfo.getLoginId();
        this.name = memberInfo.getName();
        this.phoneNo = memberInfo.getPhoneNo();
        this.email = memberInfo.getEmail();
        this.password = memberInfo.getPassword();
    }

}
