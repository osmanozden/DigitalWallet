package com.IngHubTurkey.digital_wallet.config;

import com.IngHubTurkey.digital_wallet.model.User;
import com.IngHubTurkey.digital_wallet.service.CustomerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DataInitializer implements CommandLineRunner {

    private final CustomerService customerService;

    public DataInitializer(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void run(String... args) {
        User customer = new User(null, "Ali", "Müşteri", "11111111111");
        if (customerService.getCustomerByTckn(customer.getTckn()).isEmpty()) {
            User created = customerService.createCustomer(customer);
            log.info("Customer Created! -> id={}, tckn={}", created.getId(), created.getTckn());
        }

        User employee = new User(null, "Ayşe", "Çalışan", "22222222222");
        if (customerService.getCustomerByTckn(employee.getTckn()).isEmpty()) {
            User created = customerService.createCustomer(employee);
            log.info(" Employee Created! -> id={}, tckn={}", created.getId(), created.getTckn());
        }
    }
}