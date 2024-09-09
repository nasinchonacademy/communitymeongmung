package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGamePoints is a Querydsl query type for GamePoints
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGamePoints extends EntityPathBase<GamePoints> {

    private static final long serialVersionUID = 1769771358L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGamePoints gamePoints = new QGamePoints("gamePoints");

    public final NumberPath<Long> gameseq = createNumber("gameseq", Long.class);

    public final StringPath gameType = createString("gameType");

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final NumberPath<Integer> restCount = createNumber("restCount", Integer.class);

    public final DateTimePath<java.sql.Timestamp> timePlayed = createDateTime("timePlayed", java.sql.Timestamp.class);

    public final QUser user;

    public QGamePoints(String variable) {
        this(GamePoints.class, forVariable(variable), INITS);
    }

    public QGamePoints(Path<? extends GamePoints> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGamePoints(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGamePoints(PathMetadata metadata, PathInits inits) {
        this(GamePoints.class, metadata, inits);
    }

    public QGamePoints(Class<? extends GamePoints> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

