package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private int seq;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String soscommentcontent;

    @Temporal(TemporalType.TIMESTAMP)
    private Date soscommentregtime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date soscommentupdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date soscommentdelete;

}