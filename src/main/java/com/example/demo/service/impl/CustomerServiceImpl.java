package com.example.demo.service.impl;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer addUser(Customer user) {
        return customerRepository.save(user);
    }

    @Override
    public List<Customer> getAllUsers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getUserById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void deleteUserById(Long id) {
        customerRepository.deleteById(id);
    }
}
