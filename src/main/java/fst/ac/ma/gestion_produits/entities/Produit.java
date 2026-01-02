package fst.ac.ma.gestion_produits.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor @AllArgsConstructor @Data @ToString
public class Produit {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String designation;
	private int quantite;
	private double prix;

}
