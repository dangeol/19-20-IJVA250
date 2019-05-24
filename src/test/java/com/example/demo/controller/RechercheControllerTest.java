package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RechercheController.class)
public class RechercheControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RechercheController rechercheController;

    @MockBean
    private ArticleService mockArticleService;

    @Test
    public void rechercher_pad_retourneZeroResultat() throws Exception {
        when(mockArticleService.find("pad")).thenReturn(emptyList());
        mvc.perform(get("/recherche?query=pad"))
                .andExpect(status().isOk());
        verify(mockArticleService).find("pad");
    }

    @Test
    public void rechercher_pad_retourneDeuxResultats() throws Exception {
        Article ipad = new Article();
        ipad.setLibelle("ipad");

        Article paddle = new Article();
        paddle.setLibelle("paddle");

        when(mockArticleService.find("pad")).thenReturn(
                Arrays.asList(ipad, paddle)
        );
        mvc.perform(get("/recherche?query=pad"))
                .andExpect(content().string( containsString("ipad")))
                .andExpect(content().string( containsString("paddle")))
                .andExpect(status().isOk());
        verify(mockArticleService).find("pad");
    }
}