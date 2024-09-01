package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "meongstory")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MeongStory extends BaseEntity1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", updatable = false)
    private Long seq;

    @Column(name = "title", nullable = false, length = 40)
    private String title;

    @Column(name = "content", nullable = false, length = 2500)
    private String content;

    @Column(name = "likecount", nullable = false)
    @Builder.Default
    private int likecount = 0;  // 기본값을 0으로 설정

    @Column(name = "commentcount")
    private int commentcount;

    @Column(name = "deleteddate")
    private LocalDateTime deleted;

    @Lob
    @Column(name = "picture")
    private String picture;

    @Column(name = "viewcount", nullable = false)
    private int viewcount;

    @Column(name = "category", nullable = false, length = 40)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid", nullable = false)
    private User user;

    @OneToMany(mappedBy = "storySeq")
    private Set<StoryLike> likes;

    public String getNickname() {
        return user.getNickname();
    }
}
