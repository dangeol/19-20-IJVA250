package com.example.demo.controller;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RechercheControllerIntTest {

    @Autowired
    private TestRestTemplate clientHttp;

    @Autowired
    private RechercheController rechercheController;

    @Test
    public void rechercher_pad_retourneDeuxResultats() throws Exception {
        String html = this.clientHttp.getForObject("/recherche?query=pad", String.class);
        Assertions.assertThat(html).containsSequence("Ipad PRO");
    }
}