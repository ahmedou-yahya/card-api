package com.panier.controller;

import lombok.AllArgsConstructor;

import com.panier.model.reponse.PanierReponse;
import com.panier.model.requete.PanierRequest;
import com.panier.service.PanierService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@AllArgsConstructor
public class PanierController {

    private final PanierService panierService;

    @PostMapping("/panier/synchronize")
    public PanierReponse synchroniserPanier(
            @RequestParam(required = false) String clientId,
            @RequestBody PanierRequest panierRequest){
        if(Objects.nonNull(clientId) && !clientId.isEmpty()){
            return panierService.synchroniserPanierAuth(panierRequest, clientId);
        }
        return panierService.synchroniserPanier(panierRequest);
    }
}
