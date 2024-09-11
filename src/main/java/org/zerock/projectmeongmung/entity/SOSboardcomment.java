package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
//
    @ElementCollection
    @CollectionTable(name = "soscomment_likes", joinColumns = @JoinColumn(name = "commentid"))
    @Column(name = "userid")
    private Set<Long> likedUserIds = new HashSet<>();

    // 좋아요 수
    @Column(nullable = false)
    @ColumnDefault("0")
    private int likeCount;


    @PrePersist
    protected void onCreate() {
        if (soscommentregtime == null) {
            soscommentregtime = new Date(); // 기본값으로 현재 시각을 설정
        }
    }

    // 좋아요 추가 메소드
    public void addLike(Long userId) {
        if (!likedUserIds.contains(userId)) {
            likedUserIds.add(userId);
            likeCount = likedUserIds.size();
        }
    }

}