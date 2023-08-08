package likelion.halo.hamso.dto.member;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MemberLoginDto {
    private String loginId; // 로그인 아이디
    private String password;
}
