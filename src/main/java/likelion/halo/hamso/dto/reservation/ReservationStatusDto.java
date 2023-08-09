package likelion.halo.hamso.dto.reservation;

import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.domain.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
public class ReservationStatusDto {
    private Long reservationId;
    private ReservationStatus reservationStatus;
}
