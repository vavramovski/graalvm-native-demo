package com.example.demo.service;


import com.example.demo.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product addProduct(Product product);

    Optional<Product> getProductById(Long id);

    Product updateProduct(Product product);

    List<Product> getAllProducts();

    void deleteProduct(Long id);

}
