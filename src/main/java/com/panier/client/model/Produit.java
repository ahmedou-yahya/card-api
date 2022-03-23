package com.panier.client.model;

import lombok.Data;

import com.panier.commun.Categorie;
import com.panier.commun.Marque;
import com.panier.commun.Vendeur;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Produit {
    Integer id;
    String sku;
    String codeBarre;
    String libelle;
    BigDecimal prix;
    BigDecimal prixBarre;
    List<String> images;
    String description;
    Categorie categorie;
    Vendeur vendeur;
    Boolean disponible;
    Integer stock;
    Marque marque;
    String nutriton;
}
