package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseEntity1 is a Querydsl query type for BaseEntity1
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseEntity1 extends EntityPathBase<BaseEntity1> {

    private static final long serialVersionUID = 272109524L;

    public static final QBaseEntity1 baseEntity1 = new QBaseEntity1("baseEntity1");

    public final DateTimePath<java.time.LocalDateTime> deleted = createDateTime("deleted", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> modified = createDateTime("modified", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> regdate = createDateTime("regdate", java.time.LocalDateTime.class);

    public QBaseEntity1(String variable) {
        super(BaseEntity1.class, forVariable(variable));
    }

    public QBaseEntity1(Path<? extends BaseEntity1> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntity1(PathMetadata metadata) {
        super(BaseEntity1.class, metadata);
    }

}

