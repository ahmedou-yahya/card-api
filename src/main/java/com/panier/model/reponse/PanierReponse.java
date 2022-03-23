package com.panier.model.reponse;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class PanierReponse {
    OffsetDateTime dateSynchronization;
    BigDecimal montantTotal;
    String id;
    Integer nombreProduits;
    List<ProduitReponse> produits;
}
