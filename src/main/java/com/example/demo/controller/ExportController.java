package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.service.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * Controlleur pour réaliser les exports.
 */
@Controller
@RequestMapping("/")
public class ExportController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private FactureService factureService;

    /**
     * Méthode qui écrit les données des clients dans un fichier *.csv
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/clients/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        PrintWriter writer = response.getWriter();
        List<Client> allClients = clientService.findAllClients();
        LocalDate now = LocalDate.now();
        writer.println("Id;Nom;Prenom;Date de Naissance");
        for(Client client : allClients){
            writer.println(client.getId()
                    + ";\""+client.getNom()
                    + "\";\""+client.getPrenom()
                    + "\";"+client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        }
    }

    /**
     * Méthode qui écrit les données des clients dans un fichier *.xlsx
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/clients/xlsx")
    public void clientsXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        //La ligne suivante demande au navigateur de télécharger le fichier clients.xlsx
        response.setHeader("Content-Disposition","attachment; filename=\"clients.xlsx\"");
        List<Client> allClients = clientService.findAllClients();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clients");
        Row headerRow = sheet.createRow(0);
        Cell cellNom = headerRow.createCell(0);
        Cell cellPrenom = headerRow.createCell(1);
        Cell cellDateNaissance = headerRow.createCell(2);
        cellNom.setCellValue("Nom");
        cellPrenom.setCellValue("Prenom");
        cellDateNaissance.setCellValue("Date de Naissance");
        int ligne = 1;
        for(Client client : allClients){
            Row row = sheet.createRow(ligne++);
            row.createCell(0).setCellValue(client.getNom());
            row.createCell(1).setCellValue(client.getPrenom());
            row.createCell(2).setCellValue(client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * Méthode qui écrit pour chaque client l'Id des factures et ses montants totaux en Euros dans un fichier *.xlsx
     * @param request
     * @param response
     * @param id
     * @throws IOException
     */
    @GetMapping("/clients/{id}/factures/xlsx")
    public void facturesParClientXLSX(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename=\"facturesDesClients.xlsx\"");
        List<Facture> facturesDuClient = factureService.findAllFacturesByClientId(id);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Factures");
        Row headerRow = sheet.createRow(0);
        Cell cellId = headerRow.createCell(0);
        cellId.setCellValue("Id");
        Cell cellTotal = headerRow.createCell(1);
        cellTotal.setCellValue("Prix Total");
        int ligne = 1;
        for (Facture facture : facturesDuClient) {
            Row row = sheet.createRow(ligne);
            row.createCell(0).setCellValue(facture.getId());
            row.createCell(1).setCellValue(facture.getTotal());
            ligne++;
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * Méthode qui écrit toutes les factures dans un fichier *.xlsx et groupe les onglets par client
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/factures/xlsx")
    public void toutesLesFacturesGroupesXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment; filename=\"toutesLesFactures.xlsx\"");
        //List<Facture> toutesLesFactures = factureService.findAllFactures();
        List<Client> tousLesClients = clientService.findAllClients();
        Workbook workbook = new XSSFWorkbook();
        for (Client client : tousLesClients) {
            Long id = client.getId();
            List<Facture> facturesDuClient = factureService.findAllFacturesByClientId(id);
            Sheet sheetClient = workbook.createSheet(client.getNom());
            Row rowNom = sheetClient.createRow(0);
            rowNom.createCell(0).setCellValue("Nom");
            rowNom.createCell(1).setCellValue(client.getNom());
            Row rowPrenom = sheetClient.createRow(1);
            rowPrenom.createCell(0).setCellValue("Prenom");
            rowPrenom.createCell(1).setCellValue(client.getPrenom());
            Row rowDateNaissance = sheetClient.createRow(2);
            rowDateNaissance.createCell(0).setCellValue("Date naissance");
            rowDateNaissance.createCell(1).setCellValue(client.getDateNaissance().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")));
            for (Facture facture : facturesDuClient) {
                Sheet sheet = workbook.createSheet("Facture " + facture.getId());
                Row headerRow = sheet.createRow(0);
                Cell cellNom = headerRow.createCell(0);
                cellNom.setCellValue("Nom article ");
                Cell cellQuant = headerRow.createCell(1);
                cellQuant.setCellValue("quantité");
                Cell cellPrix = headerRow.createCell(2);
                cellPrix.setCellValue("prix unitaire");
                Cell cellSoustotal = headerRow.createCell(3);
                cellSoustotal.setCellValue("prix de la ligne");
                Set<LigneFacture> ligneFactures = facture.getLigneFactures();
                int ligne = 1;
                for (LigneFacture ligneFacture : ligneFactures) {
                    Row row = sheet.createRow(ligne);
                    row.createCell(0).setCellValue(ligneFacture.getArticle().getLibelle());
                    row.createCell(1).setCellValue(ligneFacture.getQuantite());
                    row.createCell(2).setCellValue(ligneFacture.getArticle().getPrix());
                    row.createCell(3).setCellValue(ligneFacture.getSousTotal());
                    ligne++;
                }
                Row row = sheet.createRow(ligne);
                row.createCell(2).setCellValue("TOTAL");
                row.createCell(3).setCellValue(facture.getTotal());
            }
        }
        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
