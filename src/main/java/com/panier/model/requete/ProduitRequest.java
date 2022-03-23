package com.panier.model.requete;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Objects;

@Data
@Builder
public class ProduitRequest {
    Integer id;
    Integer quantite;
    OffsetDateTime dateModification;
    String commentaire;
    Boolean substitution;
}
