package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;
import org.zerock.projectmeongmung.entity.SOSboard;
import org.zerock.projectmeongmung.entity.User;

import java.util.Date;

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
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(nullable = false)
    private int likecount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date likecountupdate;
}
