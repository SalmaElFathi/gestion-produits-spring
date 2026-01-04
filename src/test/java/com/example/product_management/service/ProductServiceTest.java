package com.example.product_management.service;

import com.example.product_management.entity.Product;
import com.example.product_management.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    ProductService service;

    @Test
    void shouldReturnAllProducts() {
        when(repository.findAll()).thenReturn(List.of(new Product()));
        assertEquals(1, service.findAll().size());
    }

    @Test
    void shouldSaveProduct() {
        Product p = new Product();
        service.save(p);
        verify(repository, times(1)).save(p);
    }

    @Test
    void shouldDeleteProduct() {
        service.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
