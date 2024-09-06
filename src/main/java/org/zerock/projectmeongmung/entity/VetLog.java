package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "vet_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "logid")
    private Long logid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vetid", nullable = false)
    private Vet vet;  // 수의사와 연관된 로그

    @Column(nullable = false)
    private String logMessage;  // 로그 메시지

    @Column(nullable = false)
    private Timestamp timestamp;  // 로그 등록 시간
}
