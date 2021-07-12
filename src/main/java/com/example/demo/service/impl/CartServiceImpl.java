package com.example.demo.service.impl;

import com.example.demo.model.Cart;
import com.example.demo.model.Product;
import com.example.demo.model.Customer;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public Cart createCartForUser(Long userId) {
        Optional<Customer> user = customerRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        Cart cart = new Cart(user.get());

        return cartRepository.save(cart);
    }

    @Override
    public void processCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public Cart addProductToCart(Long id, Long productId) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        Optional<Product> productOptional = productRepository.findById(productId);
        if (cartOptional.isEmpty() || productOptional.isEmpty()) {
            throw new RuntimeException("Cart or Product Not Found");
        } else {
            Cart cart = cartOptional.get();
            if (cart.getProducts() == null) {
                cart.setProducts(new LinkedList<>());
            }

            cart.getProducts().add(productOptional.get());
            return cartRepository.save(cart);
        }
    }

    @Override
    public Cart getCartByUser(Long userId) {
        return cartRepository.findFirstByUserId(userId);
    }

    @Override
    public void removeProductFromCart(Long id, Long productId) {
        cartRepository.findById(id).ifPresent(cart -> {
            cart.getProducts().removeIf(product -> product.getId().equals(productId));
            cartRepository.save(cart);
        });
    }

}
