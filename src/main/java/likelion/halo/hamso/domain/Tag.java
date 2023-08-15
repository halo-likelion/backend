package likelion.halo.hamso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.AgriMachineType;
import likelion.halo.hamso.domain.type.ReservationStatus;
import likelion.halo.hamso.dto.reservation.ReservationInfoDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "machine_id")
    private AgriMachine machine;


    @Column(name = "tag_column")
    private String tagColumn;
}
