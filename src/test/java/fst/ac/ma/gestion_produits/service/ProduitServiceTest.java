package fst.ac.ma.gestion_produits.service;

import fst.ac.ma.gestion_produits.entities.Produit;
import fst.ac.ma.gestion_produits.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("Unitaire")
class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;
    @InjectMocks
    private ProduitService produitService;
    private Produit produit;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId(1);
        produit.setDesignation("Laptop");
        produit.setQuantite(10);
        produit.setPrix(5000.0);
    }

    @Test
    void testGetAllProduits() {
        List<Produit> produits = Arrays.asList(produit);
        when(produitRepository.findAll()).thenReturn(produits);
        List<Produit> result = produitService.getAllProduits();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(produitRepository).findAll();
    }

    @Test
    void testSaveProduit_Success() {
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);

        Produit result = produitService.saveProduit(produit);

        assertNotNull(result);
        assertEquals("Laptop", result.getDesignation());
        verify(produitRepository).save(any(Produit.class));
    }


    @Test
    void testSaveProduit_PrixNegatif() {
        produit.setPrix(-100);

        assertThrows(IllegalArgumentException.class, () -> {
            produitService.saveProduit(produit);
        });
        verify(produitRepository, never()).save(any());
    }

    @Test
    void testSaveProduit_QuantiteNegative() {
        produit.setQuantite(-5);

        assertThrows(IllegalArgumentException.class, () -> {
            produitService.saveProduit(produit);
        });
        verify(produitRepository, never()).save(any());
    }

    @Test
    void testDeleteProduit_Success() {
        when(produitRepository.existsById(1)).thenReturn(true);

        produitService.deleteProduit(1);

        verify(produitRepository).deleteById(1);
    }

    @Test
    void testDeleteProduit_NotFound() {
        when(produitRepository.existsById(999)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            produitService.deleteProduit(999);
        });
        verify(produitRepository, never()).deleteById(999);
    }

    @Test
    void testGetProduitById_Found() {
        when(produitRepository.findById(1)).thenReturn(Optional.of(produit));

        Optional<Produit> result = produitService.getProduitById(1);

        assertTrue(result.isPresent());
        assertEquals("Laptop", result.get().getDesignation());
    }

    @Test
    void testGetProduitById_NotFound() {
        when(produitRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Produit> result = produitService.getProduitById(999);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateProduit_Success() {
        Produit updatedDetails = new Produit();
        updatedDetails.setDesignation("Updated Laptop");
        updatedDetails.setQuantite(20);
        updatedDetails.setPrix(6000.0);

        when(produitRepository.findById(1)).thenReturn(Optional.of(produit));
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);

        Produit result = produitService.updateProduit(1, updatedDetails);

        assertEquals("Updated Laptop", result.getDesignation());
        assertEquals(20, result.getQuantite());
        verify(produitRepository).save(produit);
    }

  

  
}