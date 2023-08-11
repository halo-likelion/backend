package likelion.halo.hamso.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class FindPasswordDto {
    private String phoneNo;
    private String newPassword;
}
