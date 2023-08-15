package likelion.halo.hamso.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion.halo.hamso.domain.type.AgriMachineType;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "machine")
public class AgriMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "machine_type")
    private AgriMachineType type; // 농기계 종류

    @Column(name = "machine_price")
    private Integer price; // 임대 가격

    @Column(name = "machine_content")
    private String content; // 농기계 설명


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private AgriRegion region;

    @Column(name = "origin_cnt")
    @ColumnDefault("5")
    private Integer oriCnt; // 원래 개수

    @OneToMany(mappedBy = "machine", cascade = CascadeType.REMOVE)
    private List<Tag> tagList;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<AgriPossible> possibleList;
}
