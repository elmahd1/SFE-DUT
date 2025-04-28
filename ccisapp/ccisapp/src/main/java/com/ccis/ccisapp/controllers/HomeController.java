package com.ccis.ccisapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import com.ccis.ccisapp.repositories.DemarcheAdministratifRepo;
import com.ccis.ccisapp.repositories.EspaceEntrepriseRepo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class HomeController {
    @FXML private Label demarcheCount;
    @FXML private Label entrepriseCount;

    @Autowired  // Spring will inject this automatically
    private DemarcheAdministratifRepo demarcheRepo;

    @Autowired
    private EspaceEntrepriseRepo entrepriseRepo;

    @FXML
    public void initialize() {  // No parameters needed
        System.out.println("Controller created - repo is null? " + (demarcheRepo == null));
        refreshCounts();
    }

    private void refreshCounts() {
        demarcheCount.setText(String.valueOf(demarcheRepo.count()));
        entrepriseCount.setText(String.valueOf(entrepriseRepo.count()));
    }
}