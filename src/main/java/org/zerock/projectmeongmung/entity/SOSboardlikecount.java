package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;
import org.zerock.projectmeongmung.entity.SOSboard;
import org.zerock.projectmeongmung.entity.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name = "sosboardlikecount")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SOSboardlikecount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long soslikecountid;

    @ManyToOne
    @JoinColumn(name = "sosboardseq", nullable = false)
    private SOSboard sosboard;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)  // User와의 관계 설정
    private User member;

    @Column(nullable = false)
    private int likecount;

    @Column(name = "likecountupdate", nullable = false)
    private LocalDate likecountupdate;
}
