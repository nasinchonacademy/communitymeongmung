package org.zerock.projectmeongmung.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {

    private Long pid;
    private String pname;
    private  int pprice;
    private String pcategory;
    private String pdescription;
    private String pcompany;
    private int pstock;
    private String productphoto;

    // 기본 생성자
    public ProductDTO() {}

    public ProductDTO(Long pid, String pname, int pprice, String pcategory, String pdescription, String pcompany, int pstock, String productphoto) {
        this.pid = pid;
        this.pname = pname;
        this.pprice = pprice;
        this.pcategory = pcategory;
        this.pdescription = pdescription;
        this.pcompany = pcompany;
        this.pstock = pstock;
        this.productphoto = productphoto;
    }
}

