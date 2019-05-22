package com.example.demo.entity;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
    
    @ParameterizedTest
    @CsvSource({
            "0, 499.99, 0.0",
            "1, 499.99, 499.99",
            "2, 499.99, 999.98",
            "2, 0.0, 0.0"
    })
    public void getTotal_factureAvecXFoisLeMemeArticleRetournePrixUnitaireDeLArticleFoisX(int quantite, double prix, double calculTotal) {
        //Given
        Article article = new Article();
        article.setPrix(prix);

        LigneFacture ligneFacture = new LigneFacture();
        ligneFacture.setArticle(article);
        ligneFacture.setQuantite(quantite);

        Facture facture = new Facture();
        HashSet<LigneFacture> ligneFactures = new HashSet<>();

        ligneFactures.add(ligneFacture);
        facture.setLigneFactures(ligneFactures);

        //When
        Double total = facture.getTotal();

        //Then
        Assertions.assertThat(total).isEqualTo(calculTotal);
    }
}
