package likelion.halo.hamso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.Region1;
import likelion.halo.hamso.domain.type.Region2;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "region")
public class AgriRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "region1")
    private Region1 region1; // 특별시, 광역시, 도

    @Enumerated(EnumType.STRING)
    @Column(name = "region2")
    private Region2 region2; // 시, 군, 구

    @JsonIgnore
    @OneToMany(mappedBy = "machine", cascade = CascadeType.REMOVE)
    private List<AgriMachine> agriMachines;

}
