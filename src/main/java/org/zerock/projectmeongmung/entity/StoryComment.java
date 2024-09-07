package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "storycomment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class StoryComment extends BaseEntity1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentid", updatable = false)
    private Long commentid;

    @Column(name = "commentcontent", nullable = false, length = 2500)
    private String commentcontent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberid", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seq", nullable = false)
    private MeongStory story;

}
