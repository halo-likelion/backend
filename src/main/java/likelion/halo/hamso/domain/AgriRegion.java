package likelion.halo.hamso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "agriculture_information")
public class AgriRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agriculture_machine_id")
    private Long id;

    private Region1 region1; // 특별시, 광역시, 도
    private Region2 region2; // 시, 군, 구

    @JsonIgnore
    @OneToMany(mappedBy = "agriculture_machine", cascade = CascadeType.REMOVE)
    private List<AgriMachine> agriMachines;

}
