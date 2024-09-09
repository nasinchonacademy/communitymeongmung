package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1836714100L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final BooleanPath admin = createBoolean("admin");

    public final SetPath<SOSboardcomment, QSOSboardcomment> comments = this.<SOSboardcomment, QSOSboardcomment>createSet("comments", SOSboardcomment.class, QSOSboardcomment.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> dogbirthday = createDateTime("dogbirthday", java.util.Date.class);

    public final StringPath dogbreed = createString("dogbreed");

    public final StringPath dogmeeting = createString("dogmeeting");

    public final StringPath dogname = createString("dogname");

    public final StringPath email = createString("email");

    public final SetPath<GamePoints, QGamePoints> gamePoints = this.<GamePoints, QGamePoints>createSet("gamePoints", GamePoints.class, QGamePoints.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> jellypoint = createNumber("jellypoint", Integer.class);

    public final SetPath<StoryLike, QStoryLike> likes = this.<StoryLike, QStoryLike>createSet("likes", StoryLike.class, QStoryLike.class, PathInits.DIRECT2);

    public final BooleanPath locservice = createBoolean("locservice");

    public final BooleanPath marketsns = createBoolean("marketsns");

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final BooleanPath personalinfo = createBoolean("personalinfo");

    public final StringPath profilePhoto = createString("profilePhoto");

    public final SetPath<VetRecommendation, QVetRecommendation> recommendations = this.<VetRecommendation, QVetRecommendation>createSet("recommendations", VetRecommendation.class, QVetRecommendation.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> regDate = createDateTime("regDate", java.util.Date.class);

    public final ListPath<SOSboard, QSOSboard> sosBoards = this.<SOSboard, QSOSboard>createList("sosBoards", SOSboard.class, QSOSboard.class, PathInits.DIRECT2);

    public final SetPath<SOSboardlikecount, QSOSboardlikecount> soslikes = this.<SOSboardlikecount, QSOSboardlikecount>createSet("soslikes", SOSboardlikecount.class, QSOSboardlikecount.class, PathInits.DIRECT2);

    public final BooleanPath termuse = createBoolean("termuse");

    public final StringPath uid = createString("uid");

    public final BooleanPath vet = createBoolean("vet");

    public final QVet vetinfo;

    public final DateTimePath<java.util.Date> withdrawaldate = createDateTime("withdrawaldate", java.util.Date.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.vetinfo = inits.isInitialized("vetinfo") ? new QVet(forProperty("vetinfo")) : null;
    }

}

