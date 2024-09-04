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
    @Column(name = "pid")
    private Long pid;

    @Column(name = "pname")
    private String pname;

    @Column(name = "pprice")
    private int pprice;

    @Column(name = "pcategory")
    private String pcategory;

    @Column(name = "pdescription")
    private String pdescription;

    @Column(name = "pcompany")
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
