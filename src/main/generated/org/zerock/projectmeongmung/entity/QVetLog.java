package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVetLog is a Querydsl query type for VetLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVetLog extends EntityPathBase<VetLog> {

    private static final long serialVersionUID = -133194808L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVetLog vetLog = new QVetLog("vetLog");

    public final NumberPath<Long> logid = createNumber("logid", Long.class);

    public final StringPath logMessage = createString("logMessage");

    public final DateTimePath<java.sql.Timestamp> timestamp = createDateTime("timestamp", java.sql.Timestamp.class);

    public final QVet vet;

    public QVetLog(String variable) {
        this(VetLog.class, forVariable(variable), INITS);
    }

    public QVetLog(Path<? extends VetLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVetLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVetLog(PathMetadata metadata, PathInits inits) {
        this(VetLog.class, metadata, inits);
    }

    public QVetLog(Class<? extends VetLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.vet = inits.isInitialized("vet") ? new QVet(forProperty("vet"), inits.get("vet")) : null;
    }

}

