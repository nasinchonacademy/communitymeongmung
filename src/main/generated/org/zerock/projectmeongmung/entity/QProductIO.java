package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductIO is a Querydsl query type for ProductIO
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductIO extends EntityPathBase<ProductIO> {

    private static final long serialVersionUID = 234863436L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductIO productIO = new QProductIO("productIO");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final QBuy buy;

    public final NumberPath<Long> iono = createNumber("iono", Long.class);

    public final DateTimePath<java.sql.Timestamp> pdate = createDateTime("pdate", java.sql.Timestamp.class);

    public final QProduct product;

    public final EnumPath<ProductIO.Status> status = createEnum("status", ProductIO.Status.class);

    public QProductIO(String variable) {
        this(ProductIO.class, forVariable(variable), INITS);
    }

    public QProductIO(Path<? extends ProductIO> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductIO(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductIO(PathMetadata metadata, PathInits inits) {
        this(ProductIO.class, metadata, inits);
    }

    public QProductIO(Class<? extends ProductIO> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buy = inits.isInitialized("buy") ? new QBuy(forProperty("buy"), inits.get("buy")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
    }

}

