package fst.ac.ma.gestion_produits.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fst.ac.ma.gestion_produits.entities.Produit;
import fst.ac.ma.gestion_produits.repository.ProduitRepository;

@Controller
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;

    
    @GetMapping("/")
    public String index(Model model) {
        List<Produit> produits = produitRepository.findAll();
        model.addAttribute("produits", produits);
        return "index";
    }


    @PostMapping("/add")
    public String saveProduit(
            @RequestParam String designation,
            @RequestParam int quantite,
            @RequestParam double prix
    ) {
        Produit p = new Produit();
        p.setDesignation(designation);
        p.setQuantite(quantite);
        p.setPrix(prix);
        produitRepository.save(p);
        return "redirect:/";
    }


    @GetMapping("/delete")
    public String deleteProduit(@RequestParam int id) {
        produitRepository.deleteById(id);
        return "redirect:/";
    }

     @GetMapping("/edit")
    @ResponseBody
    public Produit getProduit(@RequestParam int id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));
    }

    @PostMapping("/edit")
    public String updateProduit(
            @RequestParam int id,
            @RequestParam String designation,
            @RequestParam int quantite,
            @RequestParam double prix
    ) {
        Produit p = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        p.setDesignation(designation);
        p.setQuantite(quantite);
        p.setPrix(prix);

        produitRepository.save(p);

        return "redirect:/produits";
    }
}
