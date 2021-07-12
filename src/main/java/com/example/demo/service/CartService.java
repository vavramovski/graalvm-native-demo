package com.example.demo.service;

import com.example.demo.model.Cart;

public interface CartService {

    void processCart(Long id);

    Cart addProductToCart(Long id, Long productId);

    void removeProductFromCart(Long id, Long productId);

    Cart createCartForUser(Long userId);

    Cart getCartByUser(Long userId);

}
