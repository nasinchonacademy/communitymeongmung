package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeongStory is a Querydsl query type for MeongStory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeongStory extends EntityPathBase<MeongStory> {

    private static final long serialVersionUID = 1171999470L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeongStory meongStory = new QMeongStory("meongStory");

    public final QBaseEntity1 _super = new QBaseEntity1(this);

    public final StringPath category = createString("category");

    public final NumberPath<Integer> commentcount = createNumber("commentcount", Integer.class);

    public final SetPath<StoryComment, QStoryComment> comments = this.<StoryComment, QStoryComment>createSet("comments", StoryComment.class, QStoryComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> deleted = createDateTime("deleted", java.time.LocalDateTime.class);

    public final NumberPath<Integer> likecount = createNumber("likecount", Integer.class);

    public final SetPath<StoryLike, QStoryLike> likes = this.<StoryLike, QStoryLike>createSet("likes", StoryLike.class, QStoryLike.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modified = _super.modified;

    public final StringPath picture = createString("picture");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regdate = _super.regdate;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final StringPath title = createString("title");

    public final QUser user;

    public final NumberPath<Integer> viewcount = createNumber("viewcount", Integer.class);

    public QMeongStory(String variable) {
        this(MeongStory.class, forVariable(variable), INITS);
    }

    public QMeongStory(Path<? extends MeongStory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeongStory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeongStory(PathMetadata metadata, PathInits inits) {
        this(MeongStory.class, metadata, inits);
    }

    public QMeongStory(Class<? extends MeongStory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

