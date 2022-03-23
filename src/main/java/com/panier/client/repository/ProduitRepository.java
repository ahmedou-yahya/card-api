package com.panier.client.repository;

import com.panier.client.model.Produit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;


@Repository
public class ProduitRepository {
    private final String url = "http://localhost:8080/apiproduit/produits";


    public List<Produit> getProduits(){

        RestTemplate restTemplate = new RestTemplate();
        URI uri = URI.create(url);
        HttpHeaders headers = new HttpHeaders();

        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<Produit[]>  reponse = restTemplate.exchange(uri,
                HttpMethod.GET,
                httpEntity,
                Produit[].class);
        List<Produit> produits = Arrays.asList(reponse.getBody());
        return produits;
    }

}
