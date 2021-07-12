package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
public class Product {

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Double price;

}
