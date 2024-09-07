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

    public static final QUser user = new QUser("user");

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

    public final BooleanPath termuse = createBoolean("termuse");

    public final StringPath uid = createString("uid");

    public final BooleanPath vet = createBoolean("vet");

    public final DateTimePath<java.util.Date> withdrawaldate = createDateTime("withdrawaldate", java.util.Date.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

