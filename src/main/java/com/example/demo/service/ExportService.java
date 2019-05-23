package com.example.demo.service;

import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class ExportService {
    private ClientService clientService;
    private FactureService factureService;

    public ExportService(ClientService clientService, FactureService factureService) {
        this.clientService = clientService;
        this.factureService = factureService;
    }

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


    public void exportPDF(Long idFacture, OutputStream outputSteam) throws DocumentException, IOException {
        Facture facture = factureService.findById(idFacture);

        // import com.itextpdf.text.*;
        Document document = new Document();
        PdfWriter.getInstance(document, outputSteam);
        document.open();

        PdfPTable table = new PdfPTable(3);
        table.addCell(new PdfPCell(new Phrase("Articles")));
        table.addCell(new PdfPCell(new Phrase("Quantité")));
        table.addCell(new PdfPCell(new Phrase("PrixUnitaire")));
        for (LigneFacture ligneFacture : facture.getLigneFactures()) {
            table.addCell(new PdfPCell(new Phrase(ligneFacture.getArticle().getLibelle())));
            table.addCell(new PdfPCell(new Phrase(ligneFacture.getQuantite())));
            table.addCell(new PdfPCell(new Phrase("" + ligneFacture.getArticle().getPrix())));
        }
        PdfPCell total = new PdfPCell(new Phrase("TOTAL"));
        total.setColspan(2);
        table.addCell(total);

        table.addCell(new PdfPCell(new Phrase("" + facture.getTotal())));


        document.add(table);
        document.close();
    }
}
