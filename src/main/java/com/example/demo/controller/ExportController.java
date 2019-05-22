package com.example.demo.controller;

import com.example.demo.service.ClientService;
import com.example.demo.service.FactureService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
    @Autowired
    private com.example.demo.service.ExportService exportService;

    @GetMapping("/clients/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        exportService.clientsCSV(response.getWriter());
    }

    @GetMapping("/clients/xlsx")
    public void clientsXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.xlsx\"");
        exportService.clientsXLSX(response);
    }

    @GetMapping("/clients/{id}/factures/xlsx")
    public void factureXLSXByClient(@PathVariable("id") Long clientId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures-client-" + clientId + ".xlsx\"");
        exportService.factureXLSXByClient(clientId, response);
    }
}
