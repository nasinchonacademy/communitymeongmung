package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "storylikecount")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StoryLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storylikecountid", updatable = false)
    private Long storylikecountid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid", nullable = false)
    private User user;

    // storySeq 필드에 대한 Getter 메서드
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyseq", nullable = false)
    private MeongStory storySeq;

    @Column(name = "likedate", nullable = false)
    private LocalDate likeDate; // 좋아요 날짜

}