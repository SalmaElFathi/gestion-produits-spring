package com.example.product_management;

import com.example.product_management.entity.Product;
import com.example.product_management.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductService service;

    public DataLoader(ProductService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {

        Product p1 = new Product();
        p1.setName("PC Portable");
        p1.setPrice(9000);
        p1.setQuantity(5);
        service.save(p1);

        Product p2 = new Product();
        p2.setName("Clavier");
        p2.setPrice(300);
        p2.setQuantity(20);
        service.save(p2);
    }
}
