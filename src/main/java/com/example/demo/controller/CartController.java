package com.example.demo.controller;

import com.example.demo.model.Cart;
import com.example.demo.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    @PostMapping("{userId}")
    public Cart createCart(@PathVariable Long userId) {
        return cartService.createCartForUser(userId);
    }

    @PostMapping("/process/{userId}")
    public void processCart(@PathVariable Long userId) {
        cartService.processCart(userId);
    }

    @DeleteMapping("/delete/{cartId}/{productId}")
    public void deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.removeProductFromCart(cartId, productId);
    }

}
