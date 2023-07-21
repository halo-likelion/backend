package likelion.halo.hamso.dto.Member;

import lombok.Data;

@Data
public class MemberLoginDto {
    private String loginId; // 로그인 아이디
    private String password;

    public MemberLoginDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
