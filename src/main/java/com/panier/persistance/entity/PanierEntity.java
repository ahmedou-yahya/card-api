package com.panier.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.panier.persistance.dto.PnaierDto;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_produit")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class PanierEntity {
    @Id
    private String id;

    @Type(type = "jsonb")
    @Column(name = "data", columnDefinition = "jsonb")
    private PnaierDto data;

}
