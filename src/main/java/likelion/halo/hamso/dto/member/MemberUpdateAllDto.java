package likelion.halo.hamso.dto.member;

import lombok.Data;

@Data
public class MemberUpdateAllDto {
    private String loginId; // 로그인 아이디

    private String phoneNo; // 전화번호

    private String email; // 이메일

    private String address;

    private String specificAddress;
}
