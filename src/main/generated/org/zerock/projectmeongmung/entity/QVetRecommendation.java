package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVetRecommendation is a Querydsl query type for VetRecommendation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVetRecommendation extends EntityPathBase<VetRecommendation> {

    private static final long serialVersionUID = 1079020725L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVetRecommendation vetRecommendation = new QVetRecommendation("vetRecommendation");

    public final NumberPath<Long> recommendationId = createNumber("recommendationId", Long.class);

    public final DateTimePath<java.util.Date> recommendDate = createDateTime("recommendDate", java.util.Date.class);

    public final QUser user;

    public final QVet vet;

    public QVetRecommendation(String variable) {
        this(VetRecommendation.class, forVariable(variable), INITS);
    }

    public QVetRecommendation(Path<? extends VetRecommendation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVetRecommendation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVetRecommendation(PathMetadata metadata, PathInits inits) {
        this(VetRecommendation.class, metadata, inits);
    }

    public QVetRecommendation(Class<? extends VetRecommendation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
        this.vet = inits.isInitialized("vet") ? new QVet(forProperty("vet")) : null;
    }

}

