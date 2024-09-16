package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name = "gamepoints")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class GamePoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gameseq", unique = true, nullable = false)
    private Long gameseq;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Column(name = "point", nullable = false)
    private int point;

    @Column(name = "timeplayed", nullable = false)
    private Timestamp timePlayed;

    @Column(name = "restcount", nullable = false)
    private int restCount;

    @Column(name = "gameType", nullable= false)
    private String gameType;

    @Builder
    public GamePoints(Long gameseq ,User user, String gameType, int point, Timestamp timePlayed, int restCount) {
        this.gameseq = gameseq;
        this.user = user;
        this.point = point;
        this.timePlayed = timePlayed;
        this.restCount = restCount;
        this.gameType = gameType;
    }

}
