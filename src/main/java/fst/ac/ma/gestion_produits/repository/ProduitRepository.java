package fst.ac.ma.gestion_produits.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fst.ac.ma.gestion_produits.entities.Produit;

public interface ProduitRepository extends JpaRepository<Produit,Integer>{

}
