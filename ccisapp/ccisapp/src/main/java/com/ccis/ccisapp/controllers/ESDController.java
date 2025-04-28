package com.ccis.ccisapp.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.ccis.ccisapp.models.DemarcheAdministratif;
import com.ccis.ccisapp.repositories.DemarcheAdministratifRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.*;

import org.springframework.stereotype.Component;

@Component
 public class ESDController {

//     @FXML private TableView<DemarcheAdministratif> dataTable;
//     @FXML private TableColumn<DemarcheAdministratif, String> dateContact;
//     @FXML private TableColumn<DemarcheAdministratif, String> heureContact;
//     @FXML private ComboBox<CheckBox> columnsCombo;

//     @Autowired
//     private DemarcheAdministratifRepo repository;

//     private final Map<String, TableColumn<DemarcheAdministratif, ?>> columnMap = new LinkedHashMap<>();

//     @FXML
//     public void initialize() {
//         // Setup columns
//         columnMap.put("dateContact", dateContact);
//         columnMap.put("heureContact", heureContact);

//         // Configure combo box
//         List<CheckBox> checkBoxes = new ArrayList<>();
//         columnMap.forEach((name, column) -> {
//             CheckBox cb = new CheckBox(name);
//             cb.setSelected(column.isVisible());
//             checkBoxes.add(cb);
//         });
//         columnsCombo.setItems(FXCollections.observableArrayList(checkBoxes));

//         // Load data
//        loadData();
//     }

//     private void loadData() {
//         dataTable.getItems().setAll(repository.findAll());
//     }

//     @FXML
//     public void applyColumnVisibility() {
//         columnsCombo.getItems().forEach(cb -> {
//             TableColumn<DemarcheAdministratif, ?> column = columnMap.get(cb.getText());
//             if (column != null) {
//                 column.setVisible(cb.isSelected());
//             }
//         });
//     }
}