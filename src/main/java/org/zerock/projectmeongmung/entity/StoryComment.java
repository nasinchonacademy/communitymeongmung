package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    //댓글 좋아요
    @ElementCollection
    @CollectionTable(name = "storycomment_likes", joinColumns = @JoinColumn(name = "commentid"))
    @Column(name = "userid")
    @Builder.Default
    private Set<Long> likedUserIds = new HashSet<>();

    // 좋아요 수
    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;

    // 대댓글 리스트
    @ElementCollection
    @CollectionTable(name = "storycomment_replies", joinColumns = @JoinColumn(name = "commentid") )
    @OrderBy("replyRegtime ASC")  // 대댓글 작성 시간 순으로 정렬
    @Builder.Default
    private List<StoryReply> replies = new ArrayList<>();

    // 좋아요 추가 메소드
    public void addLike(Long userId) {
        if (!likedUserIds.contains(userId)) {
            likedUserIds.add(userId);
            likeCount = likedUserIds.size();
        }
    }

    // 대댓글 추가 메소드
    public void addReply(StoryReply storyreply) {
        replies.add(storyreply);
    }

}
