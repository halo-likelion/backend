package likelion.halo.hamso.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationAssignEachMachine {
    Long reservationId;
    Long eachMachineId;
}
