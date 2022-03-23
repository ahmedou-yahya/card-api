package com.panier.model.requete;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Evenement {

    OffsetDateTime dateEvenement;
    Integer idProduit;
    Integer quantite;
    String  commentaire;
    Boolean substitution;
}
