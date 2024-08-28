package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.repository.ProductRepositrory;

import java.util.List;
import java.util.Optional;

public class ProductService {

    @Autowired
    private ProductRepositrory productRepositrory;

    public List<Product> getAllProducts(){
       return productRepositrory.findAll();
    }

    public Optional<Product> getProducctById(Long pID) {
        return productRepositrory.findById(pID);
    }

}
