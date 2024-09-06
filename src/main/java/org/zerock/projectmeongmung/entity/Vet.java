package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vetid")
    private Long vetid;

    @Column(nullable = false)
    private String vetname;

    @Column(nullable = false)
    private String animalhospitlename;

    @Column(nullable = false)
    private String registerdate;

    @Column
    private Timestamp withdrawaldate; // 탈퇴 예정일

    @Column(length = 500)
    private String profilePhoto;  // 프로필 사진 컬럼 추가

    @Column(nullable = false)
    private Boolean visibility; // 공개 여부 컬럼 (Boolean 타입)

    @ElementCollection
    @CollectionTable(name = "vet_description", joinColumns = @JoinColumn(name = "vetid"))
    @Column(name = "description")
    private List<String> description = new ArrayList<>();  // 소개 필드

    @Column(nullable = false)
    @ColumnDefault("0")
    private int recommendationCount;

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VetRecommendation> recommendations;
}
;


