package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBuy is a Querydsl query type for Buy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBuy extends EntityPathBase<Buy> {

    private static final long serialVersionUID = -217864003L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBuy buy = new QBuy("buy");

    public final StringPath detailaddress = createString("detailaddress");

    public final StringPath extraaddress = createString("extraaddress");

    public final StringPath jibunaddress = createString("jibunaddress");

    public final NumberPath<Long> orderno = createNumber("orderno", Long.class);

    public final NumberPath<Integer> postcode = createNumber("postcode", Integer.class);

    public final QProduct product;

    public final StringPath resname = createString("resname");

    public final StringPath resphone = createString("resphone");

    public final StringPath resrequirement = createString("resrequirement");

    public final StringPath roadaddress = createString("roadaddress");

    public final StringPath status = createString("status");

    public final NumberPath<Integer> totalprice = createNumber("totalprice", Integer.class);

    public final QUser user;

    public QBuy(String variable) {
        this(Buy.class, forVariable(variable), INITS);
    }

    public QBuy(Path<? extends Buy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBuy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBuy(PathMetadata metadata, PathInits inits) {
        this(Buy.class, metadata, inits);
    }

    public QBuy(Class<? extends Buy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

