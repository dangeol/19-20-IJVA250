package com.example.demo.entity;

import com.example.demo.entity.Article;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;


public class FactureTest {

    @Test
    public void getTotal_factureVideSansArticleRetourneZero() {
        //Given
        Facture facture = new Facture();
        facture.setLigneFactures(new HashSet<>());

        //When
        Double total = facture.getTotal();

        //Then
        Assertions.assertThat(total).isEqualTo(0.0);
    }

    @Test
    public void getTotal_factureAvecUnArticleRetournePrixUnitaireDeLArticle() {
        //Given
        Article article = new Article();
        article.setPrix(499.99);

        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setArticle(article);
        ligneFacture.setQuantite(1);

        Facture facture = new Facture();
        HashSet<LigneFacture> ligneFactures = new HashSet<>();

        ligneFactures.add(ligneFacture);
        facture.setLigneFactures(ligneFactures);

        //When
        Double total = facture.getTotal();

        //Then
        Assertions.assertThat(total).isEqualTo(499.99);
    }

    @Test
    public void getTotal_factureAvecDeuxFoisLeMemeArticleRetournePrixUnitaireDeLArticleFoisDeux() {
        //Given
        Article article = new Article();
        article.setPrix(499.99);

        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setArticle(article);
        ligneFacture.setQuantite(2);

        Facture facture = new Facture();
        HashSet<LigneFacture> ligneFactures = new HashSet<>();

        ligneFactures.add(ligneFacture);
        facture.setLigneFactures(ligneFactures);

        //When
        Double total = facture.getTotal();

        //Then
        Assertions.assertThat(total).isEqualTo(999.98);
    }
}
