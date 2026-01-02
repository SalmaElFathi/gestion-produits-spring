package fst.ac.ma.gestion_produits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fst.ac.ma.gestion_produits.entities.Produit;
import fst.ac.ma.gestion_produits.repository.ProduitRepository;

@SpringBootApplication

public class GestionProduitsApplication implements CommandLineRunner{
	
	public static void main(String[] args) {
		SpringApplication.run(GestionProduitsApplication.class, args);
		
		
		
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
