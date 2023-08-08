package likelion.halo.hamso.dto.reservation;

import likelion.halo.hamso.domain.Member;
import likelion.halo.hamso.domain.type.AgriMachineType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @ToString
public class ReservationCheckDto {
    private AgriMachineType machineType;
    private Long regionId;
    private int year;
    private int month;
    private int day;
}
