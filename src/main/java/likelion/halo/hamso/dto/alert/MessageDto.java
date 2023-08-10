package likelion.halo.hamso.dto.alert;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class MessageDto {
    String to;
    String code;
    String content;
}
