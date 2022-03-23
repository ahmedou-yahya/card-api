package com.panier.model.reponse;

import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.panier.client.model.Produit;
import com.panier.commun.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Data
public class ProduitReponse {
    Integer id;
    Integer quantite;
    OffsetDateTime dateModification;
    String commentaire;
    Boolean substitution;
    BigDecimal montant;
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
