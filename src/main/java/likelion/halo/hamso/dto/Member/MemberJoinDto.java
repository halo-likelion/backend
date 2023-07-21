package likelion.halo.hamso.dto.Member;

import likelion.halo.hamso.domain.Member;
import lombok.Data;

@Data
public class MemberJoinDto {
    private Long id;

    private String loginId; // 로그인 아이디

    private String password;

    private String name; // 회원의 이름

    private String phoneNo; // 전화번호

    private String email; // 이메일

    public MemberJoinDto(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.phoneNo = member.getPhoneNo();
        this.email = member.getEmail();
    }
}
