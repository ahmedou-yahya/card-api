package com.panier.service;

import lombok.AllArgsConstructor;

import com.panier.client.model.Produit;
import com.panier.client.repository.ProduitRepository;
import com.panier.mapper.PanierMapper;
import com.panier.model.reponse.PanierReponse;
import com.panier.model.reponse.ProduitReponse;
import com.panier.model.requete.Evenement;
import com.panier.model.requete.PanierRequest;
import com.panier.model.requete.ProduitRequest;
import com.panier.persistance.dto.ProduitDto;
import com.panier.persistance.entity.PanierEntity;
import com.panier.persistance.repository.PanierRepository;
import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
@AllArgsConstructor
public class PanierService {

    private final ProduitRepository produitRepository;
    private final PanierMapper panierMapper;
    private final PanierRepository panierRepository;

   public PanierReponse synchroniserPanier(PanierRequest panierRequest){
       List<Evenement> evenements = panierRequest.getEvenements();
       List<ProduitRequest> produitsRequest = panierRequest.getDernierPanier().getProduits();
       Map<Integer, ProduitRequest> oldProduits = produitsRequest.stream()
               .collect(Collectors.toMap(ProduitRequest::getId, identity()));

       List<Produit> produits = produitRepository.getProduits();

       Map<Integer, Produit> produitParId = produits.stream()
               .collect(Collectors.toMap(Produit::getId, identity()));

       List<ProduitReponse> lastProductList = mergerProduits(panierRequest.getDernierPanier().getDateSynchronization(),
               evenements,
               oldProduits,
               produitParId);

       return panierMapper.buildPanierReponse(lastProductList);
    }

    public PanierReponse synchroniserPanierAuth(PanierRequest panierRequest, String cleintId) {

        PanierEntity panierEntity = panierRepository.findById(cleintId).orElse(null);
        List<Produit> produits = produitRepository.getProduits();
        Map<Integer, Produit> produitParId = produits.stream()
                .collect(Collectors.toMap(Produit::getId, identity()));

        Map<Integer, ProduitRequest> oldProduits =new HashMap<>();
        OffsetDateTime dernierDateSynchro = OffsetDateTime.MIN;

        if(Objects.nonNull(panierEntity)){
            List<ProduitDto>  produitDtos = panierEntity.getData().getProduits();
            oldProduits = produitDtos.stream()
                    .map(panierMapper::ProduitDtoToProduitRequest)
                    .collect(Collectors.toMap(ProduitRequest::getId, identity()));
            dernierDateSynchro = panierEntity.getData().getDateSynchronization();
        }

        List<ProduitReponse> lastProductList = mergerProduits(dernierDateSynchro,
                panierRequest.getEvenements(),
                oldProduits,
                produitParId);

        PanierReponse panierReponse = panierMapper.buildPanierReponse(lastProductList);
        PanierEntity lastEntity = panierMapper.buildPanierEntity(lastProductList,
                panierReponse.getDateSynchronization(),
                cleintId);
        panierRepository.save(lastEntity);

        return panierReponse;
   }

    List<ProduitReponse> mergerProduits(OffsetDateTime dateDernierSynchro,
                                        List<Evenement> evenements,
                                        Map<Integer, ProduitRequest> oldProduits,
                                        Map<Integer, Produit> produitParId){


       //Mettre à jour les paroduits à partir des évenements ( un nouveau P ou un P mis à jour)
        List<ProduitReponse> reponseList = evenements.stream()
                .filter(evenement -> isNewEvenement(dateDernierSynchro, evenement,oldProduits.get(evenement.getIdProduit())))
                .map(evenement -> mergerEvenementAvecProduit(evenement, produitParId.get(evenement.getIdProduit()), oldProduits.get(evenement.getIdProduit())))
                .collect(Collectors.toList());

        // Lister tous les produits mis ajours avec les nouveaux produit ajouté
        List<Integer> newIds = reponseList.stream()
                .map(ProduitReponse::getId)
                .collect(Collectors.toList());
       // Retirer les produits stables ( non modifié dans cette requete
        List<ProduitRequest> oldProduitsList = new ArrayList<>(oldProduits.values());
        List<ProduitRequest> produitsStable = oldProduitsList.stream()
                .filter(produitRequest ->  !newIds.contains(produitRequest.getId()))
                .collect(Collectors.toList());

        //creer des produits réponse à partir des produit stable
        List<ProduitReponse> stableProductToProduitReponse =
                produitsStable.stream()
                .map(produitRequest -> panierMapper.toProduitReponse(produitRequest, produitParId.get(produitRequest.getId()) ))
                .collect(Collectors.toList());

        // Fusion les deux listes de produits (stable, updated and new )
        reponseList.addAll(stableProductToProduitReponse);

        return reponseList;

    }
    ProduitReponse mergerEvenementAvecProduit(Evenement evenement,Produit produit, ProduitRequest produitRequest){
       ProduitRequest temp = ProduitRequest.builder()
               .commentaire(isNull(produitRequest) ? null : produitRequest.getCommentaire())
               .substitution(isNull(produitRequest)? null : produitRequest.getSubstitution())
               .quantite(isNull(produitRequest) ? 0 : produitRequest.getQuantite())
               .build();

        String commentaire = Objects.isNull(evenement.getCommentaire()) ? temp.getCommentaire():
                evenement.getCommentaire();
        Boolean substitution = Objects.isNull(evenement.getSubstitution()) ? temp.getSubstitution():
                evenement.getSubstitution();

        Integer quantite = evenement.getQuantite() +  temp.getQuantite();
        return ProduitReponse.builder()
                .id(produit.getId())
                .quantite(quantite)
                .dateModification(OffsetDateTime.now())
                .commentaire(commentaire)
                .substitution(substitution)
                .montant(produit.getPrix().multiply(BigDecimal.valueOf(quantite)))
                .sku(produit.getSku())
                //completez les élements
                .build();
    }

    boolean isNewEvenement(OffsetDateTime dateDernierSynchro,
                           Evenement evenement,
                           ProduitRequest produitRequest){
       OffsetDateTime modificationDate = Objects.nonNull(produitRequest) ?
               produitRequest.getDateModification() : OffsetDateTime.MIN;
        return evenement.getDateEvenement().isAfter(dateDernierSynchro) &&
                evenement.getDateEvenement().isAfter(modificationDate);
   }
}
