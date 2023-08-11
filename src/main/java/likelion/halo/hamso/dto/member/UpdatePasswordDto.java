package likelion.halo.hamso.dto.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UpdatePasswordDto {
    private String oldPassword;
    private String newPassword;
}
