package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoryComment is a Querydsl query type for StoryComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoryComment extends EntityPathBase<StoryComment> {

    private static final long serialVersionUID = 1805706067L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoryComment storyComment = new QStoryComment("storyComment");

    public final QBaseEntity1 _super = new QBaseEntity1(this);

    public final StringPath commentcontent = createString("commentcontent");

    public final NumberPath<Long> commentid = createNumber("commentid", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> deleted = _super.deleted;

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final SetPath<Long, NumberPath<Long>> likedUserIds = this.<Long, NumberPath<Long>>createSet("likedUserIds", Long.class, NumberPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modified = _super.modified;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regdate = _super.regdate;

    public final ListPath<StoryReply, QStoryReply> replies = this.<StoryReply, QStoryReply>createList("replies", StoryReply.class, QStoryReply.class, PathInits.DIRECT2);

    public final QMeongStory story;

    public final QUser user;

    public QStoryComment(String variable) {
        this(StoryComment.class, forVariable(variable), INITS);
    }

    public QStoryComment(Path<? extends StoryComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoryComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoryComment(PathMetadata metadata, PathInits inits) {
        this(StoryComment.class, metadata, inits);
    }

    public QStoryComment(Class<? extends StoryComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.story = inits.isInitialized("story") ? new QMeongStory(forProperty("story"), inits.get("story")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

