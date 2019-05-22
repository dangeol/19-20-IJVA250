package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.service.ClientService;
import com.example.demo.service.FactureService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlleur pour réaliser les exports.
 */
@Service
@Transactional
public class ExportService {

    @Autowired
    private ClientService clientService;
    @Autowired
    private FactureService factureService;

    public void clientsCSV(Writer writer) throws IOException {
        PrintWriter printWriter = new PrintWriter(writer);

        List<Client> allClients = clientService.findAllClients();
        LocalDate now = LocalDate.now();
        printWriter.println("Id" + ";" + "Nom" + ";" + "Prenom" + ";" + "Date de Naissance");

        for (Client client : allClients) {
            printWriter.println(client.getId() + ";"
                    + client.getNom() + ";"
                    + client.getPrenom() + ";"
                    + client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        }
    }

    public void clientsXLSX(HttpServletResponse response) throws IOException {

        List<Client> allClients = clientService.findAllClients();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clients");
        Row headerRow = sheet.createRow(0);

        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("Id");

        Cell cellPrenom = headerRow.createCell(1);
        cellPrenom.setCellValue("Prénom");

        Cell cellNom = headerRow.createCell(2);
        cellNom.setCellValue("Nom");

        int iRow = 1;
        for (Client client : allClients) {
            Row row = sheet.createRow(iRow);

            Cell id = row.createCell(0);
            id.setCellValue(client.getId());

            Cell prenom = row.createCell(1);
            prenom.setCellValue(client.getPrenom());

            Cell nom = row.createCell(2);
            nom.setCellValue(client.getNom());

            iRow = iRow + 1;
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public void factureXLSXByClient(Long clientId, HttpServletResponse response) throws IOException {

        List<Facture> factures = factureService.findFacturesClient(clientId);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Facture");
        Row headerRow = sheet.createRow(0);

        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("Id");

        Cell cellTotal = headerRow.createCell(1);
        cellTotal.setCellValue("Prix Total");

        int iRow = 1;
        for (Facture facture : factures) {
            Row row = sheet.createRow(iRow);

            Cell id = row.createCell(0);
            id.setCellValue(facture.getId());

            Cell prenom = row.createCell(1);
            prenom.setCellValue(facture.getTotal());

            iRow = iRow + 1;
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

