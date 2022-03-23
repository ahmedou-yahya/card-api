package com.panier.persistance.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ProduitDto {
    Integer id;
    Integer quantite;
    OffsetDateTime dateModification;
    String commentaire;
    Boolean substitution;
}
