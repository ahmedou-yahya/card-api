package com.panier.persistance.dto;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class PnaierDto {
    OffsetDateTime dateSynchronization;
    String id;
    List<ProduitDto> produits;

}
