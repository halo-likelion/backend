package likelion.halo.hamso.domain;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private AgriMachine agriMachine;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag_column") // 열 이름 설정
    private String tagColumn; // 농기계 종류
}
