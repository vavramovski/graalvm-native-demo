package com.example.demo.jwt;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service(value = "userDetailsServiceImplementation")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        Customer user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
}
