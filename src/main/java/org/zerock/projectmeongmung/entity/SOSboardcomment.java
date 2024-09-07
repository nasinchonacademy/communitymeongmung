package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sosboardcomment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SOSboardcomment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentid;

    @ManyToOne
    @JoinColumn(name = "sosboardseq", nullable = false)
    private SOSboard sosboard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String soscommentcontent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date soscommentregtime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date soscommentupdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date soscommentdelete;

    @PrePersist
    protected void onCreate() {
        if (soscommentregtime == null) {
            soscommentregtime = new Date(); // 기본값으로 현재 시각을 설정
        }
    }

}