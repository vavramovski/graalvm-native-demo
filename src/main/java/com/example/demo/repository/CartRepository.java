package com.example.demo.repository;

import com.example.demo.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Customer u, Cart c where c.user.id = ?1")
    Cart findFirstByUserId(Long userId);

}
