package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSOSboardlikecount is a Querydsl query type for SOSboardlikecount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSOSboardlikecount extends EntityPathBase<SOSboardlikecount> {

    private static final long serialVersionUID = 1284398848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSOSboardlikecount sOSboardlikecount = new QSOSboardlikecount("sOSboardlikecount");

    public final NumberPath<Integer> likecount = createNumber("likecount", Integer.class);

    public final DatePath<java.time.LocalDate> likecountupdate = createDate("likecountupdate", java.time.LocalDate.class);

    public final QUser member;

    public final QSOSboard sosboard;

    public final NumberPath<Long> soslikecountid = createNumber("soslikecountid", Long.class);

    public QSOSboardlikecount(String variable) {
        this(SOSboardlikecount.class, forVariable(variable), INITS);
    }

    public QSOSboardlikecount(Path<? extends SOSboardlikecount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSOSboardlikecount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSOSboardlikecount(PathMetadata metadata, PathInits inits) {
        this(SOSboardlikecount.class, metadata, inits);
    }

    public QSOSboardlikecount(Class<? extends SOSboardlikecount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QUser(forProperty("member"), inits.get("member")) : null;
        this.sosboard = inits.isInitialized("sosboard") ? new QSOSboard(forProperty("sosboard"), inits.get("sosboard")) : null;
    }

}

