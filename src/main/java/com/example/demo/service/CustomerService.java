package com.example.demo.service;

import com.example.demo.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer addUser(Customer user);

    Optional<Customer> getUserById(Long id);

    List<Customer> getAllUsers();

    void deleteUserById(Long id);
}
