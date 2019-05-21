package com.example.demo.repository;

import com.example.demo.entity.Client;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void save() {
        //Given
        Client client = new Client();
        client.setNom("PETRILLO");
        client.setPrenom("Alexandre");
        //...

        //When
        clientRepository.save(client);

        //Then
        List<Client> allclients = clientRepository.findAll();
        Client uniqueClient = allclients.get(0);
        Assertions.assertThat(uniqueClient.getId()).isNotNull();
        Assertions.assertThat(uniqueClient.getPrenom()).isEqualTo("Alexandre");
        Assertions.assertThat(uniqueClient.getNom()).isEqualToIgnoringCase("peTRiLLO");
        Assertions.assertThat(uniqueClient.getDateNaissance()).isNull();
    }

}