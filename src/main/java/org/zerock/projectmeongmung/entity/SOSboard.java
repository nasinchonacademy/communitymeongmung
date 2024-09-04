package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "sosboard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SOSboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sosboardseq;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // User 엔티티와의 관계 설정

    @Column(nullable = false, length = 240)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date moddate;

    @Lob
    private byte[] picture;

    @Column(nullable = false)
    private int commentcount;

    @Column(nullable = false)
    private int likecount;

    @Column(nullable = false)
    private int viewcount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deldate;
}
