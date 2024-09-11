package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

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
    private String picture;

    @Column(nullable = false)
    private int commentcount=0;

    @Column(nullable = false)
    private int likecount=0;

    @Column(nullable = false)
    private int viewcount=0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deldate;

    @OneToMany(mappedBy = "sosboard", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SOSboardcomment> comments;  // 게시물에 달린 댓글들

    @PrePersist
    protected void onCreate() {
        this.regdate = new Date();  // 현재 날짜로 설정
    }

    @PreRemove
    protected void preRemove() {
        if (comments != null) {
            comments.clear();  // 삭제 전에 관련 댓글 목록을 비움
        }
    }




}
