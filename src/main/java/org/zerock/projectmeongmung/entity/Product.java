package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid", updatable = false)
    private Long pid;

    @Column(name = "pname", nullable = false)
    private String pname;

    @Column(name = "pprice", nullable = false)
    private int pprice;

    @Column(name = "pcategory", nullable = false)
    private String pcategory;

    @Column(name = "pdescription", nullable = false)
    private String pdescription;

    @Column(name = "pcompany", nullable = false)
    private String pcompany;

    @Column(name = "pstock")
    private Integer pstock;

    @Column(name = "productphoto")
    private String productphoto;

    @Builder
    public Product(String pname, int pprice, String pcategory, String pdescription, String pcompany, Integer pstock, String productphoto) {
        this.pname = pname;
        this.pprice = pprice;
        this.pcategory = pcategory;
        this.pdescription = pdescription;
        this.pcompany = pcompany;
        this.pstock = pstock;
        this.productphoto = productphoto;
    }

}
