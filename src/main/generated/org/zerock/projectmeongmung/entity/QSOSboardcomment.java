package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSOSboardcomment is a Querydsl query type for SOSboardcomment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSOSboardcomment extends EntityPathBase<SOSboardcomment> {

    private static final long serialVersionUID = -2033569913L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSOSboardcomment sOSboardcomment = new QSOSboardcomment("sOSboardcomment");

    public final NumberPath<Long> commentid = createNumber("commentid", Long.class);

    public final QSOSboard sosboard;

    public final StringPath soscommentcontent = createString("soscommentcontent");

    public final DateTimePath<java.util.Date> soscommentdelete = createDateTime("soscommentdelete", java.util.Date.class);

    public final DateTimePath<java.util.Date> soscommentregtime = createDateTime("soscommentregtime", java.util.Date.class);

    public final DateTimePath<java.util.Date> soscommentupdate = createDateTime("soscommentupdate", java.util.Date.class);

    public final QUser user;

    public QSOSboardcomment(String variable) {
        this(SOSboardcomment.class, forVariable(variable), INITS);
    }

    public QSOSboardcomment(Path<? extends SOSboardcomment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSOSboardcomment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSOSboardcomment(PathMetadata metadata, PathInits inits) {
        this(SOSboardcomment.class, metadata, inits);
    }

    public QSOSboardcomment(Class<? extends SOSboardcomment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.sosboard = inits.isInitialized("sosboard") ? new QSOSboard(forProperty("sosboard"), inits.get("sosboard")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

