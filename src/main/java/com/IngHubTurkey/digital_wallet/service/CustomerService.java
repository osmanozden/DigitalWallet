package com.IngHubTurkey.digital_wallet.service;

import com.IngHubTurkey.digital_wallet.model.User;
import com.IngHubTurkey.digital_wallet.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public User createCustomer(User customer) {
        return customerRepository.save(customer);
    }

    public Optional<User> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<User> getCustomerByTckn(String tckn) {
        return customerRepository.findByTckn(tckn);
    }

    public List<User> getAllCustomers() {
        return customerRepository.findAll();
    }
}
