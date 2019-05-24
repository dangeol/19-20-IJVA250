package com.example.demo.service;

import com.example.demo.entity.Client;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Collections;


@RunWith(MockitoJUnitRunner.class)
public class ExportServiceTest {

    @Mock
    private ClientService mockClientService;

    @InjectMocks
    private ExportService exportService;

    @Test
    public void clientCSV_avecZeroClient() throws IOException {
        //Given

        //When//Then
        StringWriter stringWriter = new StringWriter();
        exportService.clientsCSV(stringWriter);

        Assertions.assertThat(stringWriter.toString()).contains("Id;Nom;");
    }

    @Test
    public void testClientCSV_avecUnClient() throws IOException {
        //Given
        Client client = new Client();
        client.setNom("PETRILLO");
        client.setPrenom("Alexandre");
        client.setDateNaissance(LocalDate.now());

        //When
        Mockito.when(mockClientService.findAllClients()).thenReturn(Collections.singletonList(client));

        //Then
        StringWriter stringWriter = new StringWriter();
        exportService.clientsCSV(stringWriter);
        Assertions.assertThat(stringWriter.toString()).contains("PETRILLO;Alexandre;");
    }

    @Test
    //Ce test ne marche pas
    public void testClientsXLSX_avecZeroClient() throws IOException {
        //Given

        //When //Then
        MockHttpServletResponse response = new MockHttpServletResponse();
        exportService.clientsXLSX(response.getOutputStream());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clients");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Id");
        headerRow.createCell(1).setCellValue("Prénom");

        Assertions.assertThat(response.getContentAsString()).contains("Id;Nom;");
    }

}
