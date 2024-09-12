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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVet vet = new QVet("vet");

    public final BooleanPath accountNonExpired = createBoolean("accountNonExpired");

    public final BooleanPath accountNonLocked = createBoolean("accountNonLocked");

    public final StringPath animalhospitlename = createString("animalhospitlename");

    public final CollectionPath<org.springframework.security.core.GrantedAuthority, SimplePath<org.springframework.security.core.GrantedAuthority>> authorities = this.<org.springframework.security.core.GrantedAuthority, SimplePath<org.springframework.security.core.GrantedAuthority>>createCollection("authorities", org.springframework.security.core.GrantedAuthority.class, SimplePath.class, PathInits.DIRECT2);

    public final BooleanPath credentialsNonExpired = createBoolean("credentialsNonExpired");

    public final ListPath<String, StringPath> description = this.<String, StringPath>createList("description", String.class, StringPath.class, PathInits.DIRECT2);

    public final StringPath email = createString("email");

    public final BooleanPath enabled = createBoolean("enabled");

    public final StringPath password = createString("password");

    public final StringPath profilePhoto = createString("profilePhoto");

    public final NumberPath<Integer> recommendationCount = createNumber("recommendationCount", Integer.class);

    public final SetPath<VetRecommendation, QVetRecommendation> recommendations = this.<VetRecommendation, QVetRecommendation>createSet("recommendations", VetRecommendation.class, QVetRecommendation.class, PathInits.DIRECT2);

    public final StringPath registerdate = createString("registerdate");

    public final QUser user;

    public final StringPath username = createString("username");

    public final NumberPath<Long> vetid = createNumber("vetid", Long.class);

    public final ListPath<VetLog, QVetLog> vetLogs = this.<VetLog, QVetLog>createList("vetLogs", VetLog.class, QVetLog.class, PathInits.DIRECT2);

    public final StringPath vetname = createString("vetname");

    public final BooleanPath visibility = createBoolean("visibility");

    public final DateTimePath<java.sql.Timestamp> withdrawaldate = createDateTime("withdrawaldate", java.sql.Timestamp.class);

    public QVet(String variable) {
        this(Vet.class, forVariable(variable), INITS);
    }

    public QVet(Path<? extends Vet> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVet(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVet(PathMetadata metadata, PathInits inits) {
        this(Vet.class, metadata, inits);
    }

    public QVet(Class<? extends Vet> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

