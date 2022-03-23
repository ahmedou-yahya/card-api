package com.panier.model.requete;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class DernierPanier {
    OffsetDateTime dateSynchronization;
    List<ProduitRequest> produits;
}
