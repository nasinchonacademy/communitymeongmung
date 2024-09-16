package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QReply extends BeanPath<Reply> {

    private static final long serialVersionUID = 1100385121L;

    public static final QReply reply = new QReply("reply");

    public final StringPath id = createString("id");

    public final StringPath replyContent = createString("replyContent");

    public final DateTimePath<java.util.Date> replyRegtime = createDateTime("replyRegtime", java.util.Date.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QReply(String variable) {
        super(Reply.class, forVariable(variable));
    }

    public QReply(Path<? extends Reply> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReply(PathMetadata metadata) {
        super(Reply.class, metadata);
    }

}

