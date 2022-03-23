package com.panier.model.requete;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class PanierRequest {
    OffsetDateTime dateClient;
    DernierPanier dernierPanier;
    List<Evenement> evenements;
}
