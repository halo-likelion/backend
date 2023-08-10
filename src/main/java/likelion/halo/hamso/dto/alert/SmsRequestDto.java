package likelion.halo.hamso.dto.alert;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SmsRequestDto {
    String type;
    String contentType;
    String countryCode;
    String from;
    String content;
    List<MessageDto> messages;
}
