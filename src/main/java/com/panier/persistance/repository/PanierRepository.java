package com.panier.persistance.repository;

import com.panier.persistance.entity.PanierEntity;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PanierRepository  extends JpaRepository<PanierEntity, String> {
}
