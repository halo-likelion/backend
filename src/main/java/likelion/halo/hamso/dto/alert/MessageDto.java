package likelion.halo.hamso.dto.alert;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MessageDto {
    String to;
    String content;
}
