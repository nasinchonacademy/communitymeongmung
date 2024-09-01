package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDeliveryLoc is a Querydsl query type for DeliveryLoc
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDeliveryLoc extends EntityPathBase<DeliveryLoc> {

    private static final long serialVersionUID = -1385357853L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDeliveryLoc deliveryLoc = new QDeliveryLoc("deliveryLoc");

    public final StringPath etcAddr = createString("etcAddr");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath localAddr = createString("localAddr");

    public final StringPath roadAddr = createString("roadAddr");

    public final QUser user;

    public QDeliveryLoc(String variable) {
        this(DeliveryLoc.class, forVariable(variable), INITS);
    }

    public QDeliveryLoc(Path<? extends DeliveryLoc> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDeliveryLoc(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDeliveryLoc(PathMetadata metadata, PathInits inits) {
        this(DeliveryLoc.class, metadata, inits);
    }

    public QDeliveryLoc(Class<? extends DeliveryLoc> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

