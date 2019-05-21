package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;
import com.example.demo.service.ExportService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;


@RunWith(MockitoJUnitRunner.class)
public class ExportControllerTest {

    @Mock
    private ClientService mockClientService;

    @InjectMocks
    private ExportService exportService;

    @Test
    public void clientCSV_avecZeroClient() throws IOException {
        StringWriter stringWriter = new StringWriter();
        exportService.clientsCSV(stringWriter);
        Assertions.assertThat(stringWriter.toString()).contains("Id;Nom;");
    }

    @Test
    public void clientCSV_avecUnClient() throws IOException {
        Client client = new Client();
        client.setNom("PETRILLO");
        client.setPrenom("Alexandre");
        client.setDateNaissance(LocalDate.now());

        Mockito.when(mockClientService.findAllClients()).thenReturn(Collections.singletonList(client));

        StringWriter stringWriter = new StringWriter();
        exportService.clientsCSV(stringWriter);
        Assertions.assertThat(stringWriter.toString()).contains("PETRILLO;Alexandre;");
    }

}