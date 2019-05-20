package com.example.demo.entity;

import javax.persistence.*;

/**
 * Created by Alexandre on 09/04/2018.
 */
@Entity
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Facture facture;

    @ManyToOne
    private Article article;

    private int quantite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Facture getFacture() { return facture; }

    public void setFacture(Facture facture) { this.facture = facture; }

    public void setQuantite(int quantite) { this.quantite = quantite; }

    public Double getSousTotal() {

        return this.quantite * this.article.getPrix();
    }
}
