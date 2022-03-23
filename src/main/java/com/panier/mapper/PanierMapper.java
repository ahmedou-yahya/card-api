package com.panier.mapper;

import com.panier.client.model.Produit;
import com.panier.model.reponse.PanierReponse;
import com.panier.model.reponse.ProduitReponse;
import com.panier.model.requete.ProduitRequest;
import com.panier.persistance.dto.PnaierDto;
import com.panier.persistance.dto.ProduitDto;
import com.panier.persistance.entity.PanierEntity;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PanierMapper {

    public PanierReponse buildPanierReponse(List<ProduitReponse> produits){
        BigDecimal montantTotal = produits.stream()
                .map(produitReponse -> produitReponse.getMontant())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return PanierReponse.builder()
                .dateSynchronization(OffsetDateTime.now())
                .montantTotal(montantTotal)
                .id(UUID.randomUUID().toString())
                .nombreProduits(produits.size())
                .produits(produits)
                .build();

    }
    public PanierEntity buildPanierEntity(List<ProduitReponse> produits,
                                          OffsetDateTime synchroDate,
                                          String id){

        List<ProduitDto> produitDtos = produits.stream()
                .map(produitReponse -> produitProduitDtoFromResponse(produitReponse))
                .collect(Collectors.toList());
        PnaierDto panierDto = buildPanierDto(id, produitDtos, synchroDate);
        return PanierEntity.builder()
                .id(id)
                .data(panierDto).build();
    }
    PnaierDto buildPanierDto(String id, List<ProduitDto> produitDtos, OffsetDateTime dateSynchro){
        return PnaierDto.builder()
                .id(id)
                .dateSynchronization(dateSynchro)
                .produits(produitDtos)
                .build();

    }

    ProduitDto produitProduitDtoFromResponse(ProduitReponse produitReponse){
        return  ProduitDto.builder()
                .id(produitReponse.getId())
                .commentaire(produitReponse.getCommentaire())
                .quantite(produitReponse.getQuantite())
                .substitution(produitReponse.getSubstitution())
                .dateModification(produitReponse.getDateModification())
                .build();
    }

    public ProduitReponse toProduitReponse(ProduitRequest produitRequest, Produit produit){
       return ProduitReponse.builder()
                .id(produit.getId())
                .quantite(produitRequest.getQuantite())
                .dateModification(produitRequest.getDateModification())
                .commentaire(produitRequest.getCommentaire())
                .substitution(produitRequest.getSubstitution())
                .montant(produit.getPrix().multiply(BigDecimal.valueOf(produitRequest.getQuantite())))
                .sku(produit.getSku())
                //complete les élements
                .build();
    }
    public ProduitRequest ProduitDtoToProduitRequest(ProduitDto produitDto){
        return ProduitRequest.builder()
                .id(produitDto.getId())
                .quantite(produitDto.getQuantite())
                .dateModification(produitDto.getDateModification())
                .commentaire(produitDto.getCommentaire())
                .substitution(produitDto.getSubstitution())
                .build();
    }
}
