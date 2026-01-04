package fst.ac.ma.gestion_produits.repository;

import fst.ac.ma.gestion_produits.entities.Produit;
import fst.ac.ma.gestion_produits.repository.ProduitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;

@SpringBootTest
@ActiveProfiles("test")
@Tag("Integration")
public class ProduitIntegrationTest {
    
    @Autowired
    private ProduitRepository produitRepository;
    
    @Test
    public void testSaveProduit() {
        Produit produit = new Produit();
        produit.setDesignation("Laptop Test");
        produit.setQuantite(5);
        produit.setPrix(5000.0);
        
        Produit saved = produitRepository.save(produit);
        
        assertNotNull(saved.getId());
        assertTrue(produitRepository.findById(saved.getId()).isPresent());
    }
    
    @Test
    public void testFindAndDelete() {
        Produit produit = new Produit();
        produit.setDesignation("Test Delete");
        produit.setQuantite(10);
        produit.setPrix(1000.0);
        Produit saved = produitRepository.save(produit);
        
        produitRepository.deleteById(saved.getId());
        
        assertFalse(produitRepository.findById(saved.getId()).isPresent());
    }
    @AfterEach
    void cleanup() {
        produitRepository.deleteAll();
    }
}