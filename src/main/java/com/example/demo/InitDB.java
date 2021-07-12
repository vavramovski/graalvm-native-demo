package com.example.demo;

import com.example.demo.model.Cart;
import com.example.demo.model.Customer;
import com.example.demo.model.Product;
import com.example.demo.service.CartService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@AllArgsConstructor
public class InitDB {

    private final ProductService productService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        Customer user = new Customer("Viktor", "123456");
        Product product = new Product("LED Sijalica", 200d);
        product = productService.addProduct(product);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = customerService.addUser(user);
        Cart cart = cartService.createCartForUser(user.getId());
        cartService.addProductToCart(cart.getId(), product.getId());
    }
}
