package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "storycomment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StoryComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentid", updatable = false)
    private Long commentid;

    @Column(name = "commentcontent", nullable = false, length = 2500)
    private String commentcontent;

    @Column(name = "commentregitime", nullable = false)
    private LocalDateTime commentregitime;

    @Column(name = "commentupdate", nullable = false)
    private LocalDateTime commentupdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq", nullable = false)  // 외래 키 설정
    private MeongStory story;  // `MeongStory` 엔티티와의 관계 설정
}
