package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStoryReply is a Querydsl query type for StoryReply
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QStoryReply extends BeanPath<StoryReply> {

    private static final long serialVersionUID = -1580092194L;

    public static final QStoryReply storyReply = new QStoryReply("storyReply");

    public final StringPath id = createString("id");

    public final StringPath replyContent = createString("replyContent");

    public final DateTimePath<java.util.Date> replyRegtime = createDateTime("replyRegtime", java.util.Date.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QStoryReply(String variable) {
        super(StoryReply.class, forVariable(variable));
    }

    public QStoryReply(Path<? extends StoryReply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStoryReply(PathMetadata metadata) {
        super(StoryReply.class, metadata);
    }

}

