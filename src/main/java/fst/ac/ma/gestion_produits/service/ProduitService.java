package fst.ac.ma.gestion_produits.service;

import fst.ac.ma.gestion_produits.entities.Produit;
import fst.ac.ma.gestion_produits.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {
    
    @Autowired
    private ProduitRepository produitRepository;
    
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }
    
    public Produit saveProduit(Produit produit) {
        if (produit.getPrix() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        if (produit.getQuantite() < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
        return produitRepository.save(produit);
    }
    
    public void deleteProduit(int id) {
        if (!produitRepository.existsById(id)) {
            throw new RuntimeException("Produit avec id " + id + " n'existe pas");
        }
        produitRepository.deleteById(id);
    }
    
    public Optional<Produit> getProduitById(int id) {
        return produitRepository.findById(id);
    }
    
    public Produit updateProduit(int id, Produit produitDetails) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec id: " + id));
        
        if (produitDetails.getPrix() < 0) {
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        if (produitDetails.getQuantite() < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
        
        produit.setDesignation(produitDetails.getDesignation());
        produit.setQuantite(produitDetails.getQuantite());
        produit.setPrix(produitDetails.getPrix());
        
        return produitRepository.save(produit);
    }
    
    public double calculerValeurStock() {
        List<Produit> produits = produitRepository.findAll();
        return produits.stream()
                .mapToDouble(p -> p.getPrix() * p.getQuantite())
                .sum();
    }
    
    public List<Produit> getProduitsEnRupture() {
        List<Produit> produits = produitRepository.findAll();
        return produits.stream()
                .filter(p -> p.getQuantite() == 0)
                .toList();
    }
}