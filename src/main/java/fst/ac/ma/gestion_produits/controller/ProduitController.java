package fst.ac.ma.gestion_produits.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fst.ac.ma.gestion_produits.entities.Produit;
import fst.ac.ma.gestion_produits.service.ProduitService;

@Controller
public class ProduitController {
    
    @Autowired
    private ProduitService produitService;
    
    @GetMapping("/")
    public String index(Model model) {
        List<Produit> produits = produitService.getAllProduits();
        model.addAttribute("produits", produits);
        return "index";
    }
    
    @PostMapping("/add")
    public String saveProduit(
            @RequestParam String designation,
            @RequestParam int quantite,
            @RequestParam double prix,
            Model model
    ) {
        try {
            Produit p = new Produit();
            p.setDesignation(designation);
            p.setQuantite(quantite);
            p.setPrix(prix);
            produitService.saveProduit(p);
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }
    
    @GetMapping("/delete")
    public String deleteProduit(@RequestParam int id, Model model) {
        try {
            produitService.deleteProduit(id);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
    
    @GetMapping("/edit")
    @ResponseBody
    public Produit getProduit(@RequestParam int id) {
        return produitService.getProduitById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }
    
    @PostMapping("/edit")
    public String updateProduit(
            @RequestParam int id,
            @RequestParam String designation,
            @RequestParam int quantite,
            @RequestParam double prix,
            Model model
    ) {
        try {
            Produit produitDetails = new Produit();
            produitDetails.setDesignation(designation);
            produitDetails.setQuantite(quantite);
            produitDetails.setPrix(prix);
            
            produitService.updateProduit(id, produitDetails);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
}