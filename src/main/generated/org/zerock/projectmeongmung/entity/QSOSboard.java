package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSOSboard is a Querydsl query type for SOSboard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSOSboard extends EntityPathBase<SOSboard> {

    private static final long serialVersionUID = 940653528L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSOSboard sOSboard = new QSOSboard("sOSboard");

    public final NumberPath<Integer> commentcount = createNumber("commentcount", Integer.class);

    public final SetPath<SOSboardcomment, QSOSboardcomment> comments = this.<SOSboardcomment, QSOSboardcomment>createSet("comments", SOSboardcomment.class, QSOSboardcomment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final DateTimePath<java.util.Date> deldate = createDateTime("deldate", java.util.Date.class);

    public final NumberPath<Integer> likecount = createNumber("likecount", Integer.class);

    public final DateTimePath<java.util.Date> moddate = createDateTime("moddate", java.util.Date.class);

    public final StringPath picture = createString("picture");

    public final DateTimePath<java.util.Date> regdate = createDateTime("regdate", java.util.Date.class);

    public final NumberPath<Long> sosboardseq = createNumber("sosboardseq", Long.class);

    public final StringPath title = createString("title");

    public final QUser user;

    public final NumberPath<Integer> viewcount = createNumber("viewcount", Integer.class);

    public QSOSboard(String variable) {
        this(SOSboard.class, forVariable(variable), INITS);
    }

    public QSOSboard(Path<? extends SOSboard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSOSboard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSOSboard(PathMetadata metadata, PathInits inits) {
        this(SOSboard.class, metadata, inits);
    }

    public QSOSboard(Class<? extends SOSboard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

