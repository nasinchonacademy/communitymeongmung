package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVet is a Querydsl query type for Vet
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVet extends EntityPathBase<Vet> {

    private static final long serialVersionUID = -217845284L;

    public static final QVet vet = new QVet("vet");

    public final StringPath animalhospitlename = createString("animalhospitlename");

    public final ListPath<String, StringPath> description = this.<String, StringPath>createList("description", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath profilePhoto = createString("profilePhoto");

    public final NumberPath<Integer> recommendationCount = createNumber("recommendationCount", Integer.class);

    public final SetPath<VetRecommendation, QVetRecommendation> recommendations = this.<VetRecommendation, QVetRecommendation>createSet("recommendations", VetRecommendation.class, QVetRecommendation.class, PathInits.DIRECT2);

    public final StringPath registerdate = createString("registerdate");

    public final NumberPath<Long> vetid = createNumber("vetid", Long.class);

    public final StringPath vetname = createString("vetname");

    public final BooleanPath visibility = createBoolean("visibility");

    public final DateTimePath<java.sql.Timestamp> withdrawaldate = createDateTime("withdrawaldate", java.sql.Timestamp.class);

    public QVet(String variable) {
        super(Vet.class, forVariable(variable));
    }

    public QVet(Path<? extends Vet> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVet(PathMetadata metadata) {
        super(Vet.class, metadata);
    }

}

