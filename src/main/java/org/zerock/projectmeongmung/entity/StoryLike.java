package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "StoryLikecount")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storyseq", nullable = false)
    private MeongStory storySeq;

    // storySeq 필드에 대한 Getter 메서드
    public MeongStory getStorySeq() {
        return storySeq;
    }

}
