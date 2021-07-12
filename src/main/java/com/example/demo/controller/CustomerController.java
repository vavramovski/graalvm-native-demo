package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public Customer addUser(@RequestBody Customer user) {
        return customerService.addUser(user);
    }

    @GetMapping
    public List<Customer> getAllUsers() {
        return customerService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<Customer> getUserById(@PathVariable Long id) {
        return customerService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        customerService.deleteUserById(id);
    }

}
