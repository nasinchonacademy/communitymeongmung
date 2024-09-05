package org.zerock.projectmeongmung.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCartId is a Querydsl query type for CartId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCartId extends BeanPath<CartId> {

    private static final long serialVersionUID = -680865084L;

    public static final QCartId cartId = new QCartId("cartId");

    public final NumberPath<Long> pID = createNumber("pID", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCartId(String variable) {
        super(CartId.class, forVariable(variable));
    }

    public QCartId(Path<? extends CartId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCartId(PathMetadata metadata) {
        super(CartId.class, metadata);
    }

}

