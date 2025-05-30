package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import ccis.controllers.RapportDemarcheController.RecetteRow;
import ccis.controllers.RapportDemarcheController.SimpleRow;

public class RapportEspaceController {

    @FXML private DatePicker date1;
    @FXML private DatePicker date2;
    @FXML private DatePicker date3;

    // Table Back Office
    @FXML private TableView<RapportRow> bo;
    @FXML private TableColumn<RapportRow, String> col1;
    @FXML private TableColumn<RapportRow, String> col2;
    @FXML private TableColumn<RapportRow, Float> col3;
    @FXML private TableColumn<RapportRow, Float> col4;
    @FXML private TableColumn<RapportRow, String> col5;
    @FXML private TableColumn<RapportRow, String> col6;

    // Table 2
    @FXML private TableView<IndicateurRow> table2;
    @FXML private TableColumn<IndicateurRow, String> col7;
    @FXML private TableColumn<IndicateurRow, Float> col8;
    @FXML private TableColumn<IndicateurRow, Float> col9;
    @FXML private TableColumn<IndicateurRow, String> col10;

    // Table FO
    @FXML private TableView<RapportRow> fo;
    @FXML private TableColumn<RapportRow, String> col11;
    @FXML private TableColumn<RapportRow, String> col12;
    @FXML private TableColumn<RapportRow, Float> col13;
    @FXML private TableColumn<RapportRow, Float> col14;
    @FXML private TableColumn<RapportRow, String> col15;
    @FXML private TableColumn<RapportRow, String> col16;

    // Table 4
    @FXML private TableView<IndicateurRow> table4;
    @FXML private TableColumn<IndicateurRow, String> col17;
    @FXML private TableColumn<IndicateurRow, Float> col18;
    @FXML private TableColumn<IndicateurRow, Float> col19;
    @FXML private TableColumn<IndicateurRow, String> col20;

    // Charts
    @FXML private BarChart<String, Number> chart1;
    @FXML private CategoryAxis xAxis1;
    @FXML private NumberAxis yAxis1;

    @FXML private BarChart<String, Number> chart2;
    @FXML private CategoryAxis xAxis2;
    @FXML private NumberAxis yAxis2;

    // Rapport text area & bouton
    @FXML private TextArea rapportTextArea;
    @FXML private Button btnGenerer;

    // Data storage
    private ObservableList<RapportRow> boRows;
    private ObservableList<IndicateurRow> table2Rows;
    private ObservableList<RapportRow> foRows;
    private ObservableList<IndicateurRow> table4Rows;

    @FXML
    public void initialize() {
        date1.setValue(LocalDate.now().minusMonths(1));
        date2.setValue(LocalDate.now());

        setupBackOfficeTable();
        setupTable2();
        setupFrontOfficeTable();
        setupTable4();
        setupButtonAction();
    }

    private void setupBackOfficeTable() {
        // Set cell value factories
        col1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol1()));
        col2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol2()));
        col3.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol3()).asObject());
        col4.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol4()).asObject());
        col5.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol5()));
        col6.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol6()));

        // Make columns editable
        col1.setCellFactory(TextFieldTableCell.forTableColumn());
        col2.setCellFactory(TextFieldTableCell.forTableColumn());
        col3.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col4.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col5.setCellFactory(TextFieldTableCell.forTableColumn());
        col6.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set edit commit handlers
        col1.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol1(event.getNewValue());
        });
        col2.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol2(event.getNewValue());
        });
        col3.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol3(event.getNewValue());
        });
        col4.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol4(event.getNewValue());
        });
        col5.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol5(event.getNewValue());
        });
        col6.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol6(event.getNewValue());
        });

        // Initialize data
        boRows = FXCollections.observableArrayList(
            new RapportRow("Nombre de programmes répertoriés", "Annuelle", 0f, 0f, "", ""),
            new RapportRow("Nombre de démarches administratives répertoriés", "Annuelle", 0f, 0f, "", ""),
            new RapportRow("Nombre d'entreprises répertoriés (Annuaire des entreprises)", "Annuelle", 0f, 0f, "", ""),
            new RapportRow("Nombre d'administrations répertoriées (Répertoire des administrations)", "Annuelle", 0f, 0f, "", "")
        );
        bo.setItems(boRows);
    }

    private void setupTable2() {
        // Set cell value factories
        col7.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIndicateur()));
        col8.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getObjectif()).asObject());
        col9.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getRealise()).asObject());
        col10.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPourcentage()));

        // Make columns editable
        col7.setCellFactory(TextFieldTableCell.forTableColumn());
        col8.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col9.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col10.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set edit commit handlers
        col7.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setIndicateur(event.getNewValue());
        });
        col8.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setObjectif(event.getNewValue());
            updatePercentage(row);
        });
        col9.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setRealise(event.getNewValue());
            updatePercentage(row);
            table2.refresh();
        });

        // Initialize data
        table2Rows = FXCollections.observableArrayList(
            new IndicateurRow("Nombre de programmes répertoriés", 0f, 0f, "0%"),
            new IndicateurRow("Nombre de démarches administratives répertoriés", 0f, 0f, "0%"),
            new IndicateurRow("Nombre d'entreprises répertoriés (Annuaire des entreprises)", 0f, 0f, "0%"),
            new IndicateurRow("Nombre d'administrations répertoriées (Répertoire des administrations)", 0f, 0f, "0%")
        );
        table2.setItems(table2Rows);
    }

    private void setupFrontOfficeTable() {
        // Set cell value factories
        col11.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol1()));
        col12.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol2()));
        col13.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol3()).asObject());
        col14.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol4()).asObject());
        col15.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol5()));
        col16.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol6()));

        // Make columns editable
        col11.setCellFactory(TextFieldTableCell.forTableColumn());
        col12.setCellFactory(TextFieldTableCell.forTableColumn());
        col13.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col14.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col15.setCellFactory(TextFieldTableCell.forTableColumn());
        col16.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set edit commit handlers (similar to BO table)
        col11.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol1(event.getNewValue());
        });
        col12.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol2(event.getNewValue());
        });
        col13.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol3(event.getNewValue());
        });
        col14.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol4(event.getNewValue());
        });
        col15.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol5(event.getNewValue());
        });
        col16.setOnEditCommit(event -> {
            RapportRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setCol6(event.getNewValue());
        });

        // Initialize data
        foRows = FXCollections.observableArrayList(
            new RapportRow("Nombre de ressortissants accueillis", "Mensuelle", 500f, 25f, "Comptage journalier", "Accueil"),
            new RapportRow("Nombre de prestations rendues pour les programmes à l'attention des entreprises", "Mensuelle", 300f, 20f, "Système de suivi", "Équipe FO"),
            new RapportRow("Nombre de prestations rendues pour les démarches administratives", "Mensuelle", 400f, 30f, "Système de suivi", "Équipe FO"),
            new RapportRow("Nombre de prestations rendues pour l'annuaire des entreprises", "Mensuelle", 200f, 15f, "Logs système", "Équipe FO"),
            new RapportRow("Nombre de prestations rendues pour le répertoire des administrations", "Mensuelle", 100f, 10f, "Logs système", "Équipe FO")
        );
        fo.setItems(foRows);
    }

    private void setupTable4() {
        // Set cell value factories
        col17.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIndicateur()));
        col18.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getObjectif()).asObject());
        col19.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getRealise()).asObject());
        col20.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPourcentage()));

        // Make columns editable
        col17.setCellFactory(TextFieldTableCell.forTableColumn());
        col18.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col19.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col20.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set edit commit handlers
        col17.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setIndicateur(event.getNewValue());
        });
        col18.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setObjectif(event.getNewValue());
            updatePercentage(row);
        });
        col19.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setRealise(event.getNewValue());
            updatePercentage(row);
            table4.refresh();
        });

        // Initialize data - FIXED: was setting to table2 instead of table4
        table4Rows = FXCollections.observableArrayList(
            new IndicateurRow("Nombre de ressortissants accueillis", 500f, 480f, "96%"),
            new IndicateurRow("Nombre de prestations rendues pour les programmes à l'attention des entreprises", 300f, 275f, "92%"),
            new IndicateurRow("Nombre de prestations rendues pour les démarches administratives", 400f, 390f, "98%"),
            new IndicateurRow("Nombre de prestations rendues pour l'annuaire des entreprises", 200f, 185f, "93%"),
            new IndicateurRow("Nombre de prestations rendues pour le répertoire des administrations", 100f, 95f, "95%")
        );
        table4.setItems(table4Rows);
    }

    private void setupButtonAction() {
        btnGenerer.setOnAction(event -> generateReport());
    }

    private void updatePercentage(IndicateurRow row) {
        if (row.getObjectif() != 0) {
            float percentage = (row.getRealise() / row.getObjectif()) * 100;
            row.setPourcentage(String.format("%.1f%%", percentage));
        } else {
            row.setPourcentage("N/A");
        }
    }

    private void generateReport() {
        // Generate charts
        generateChart1();
        generateChart2();
        

    }

    private void generateChart1() {
        chart1.getData().clear();
        
        // Configure the category axis
        xAxis1.setCategories(FXCollections.<String>observableArrayList());
        
        XYChart.Series<String, Number> objectifSeries = new XYChart.Series<>();
        objectifSeries.setName("Objectifs");
        
        XYChart.Series<String, Number> realiseSeries = new XYChart.Series<>();
        realiseSeries.setName("Réalisé");

        // Create categories list for proper axis configuration
        ObservableList<String> categories = FXCollections.observableArrayList();
        
        for (IndicateurRow row : table2Rows) {
            String shortName = shortenName(row.getIndicateur());
            categories.add(shortName);
            objectifSeries.getData().add(new XYChart.Data<>(shortName, row.getObjectif()));
            realiseSeries.getData().add(new XYChart.Data<>(shortName, row.getRealise()));
        }

        // Set categories to the axis
        xAxis1.setCategories(categories);
        xAxis1.setTickLabelRotation(-45); // Rotate labels to avoid overlap
        
        chart1.getData().addAll(objectifSeries, realiseSeries);
        chart1.setTitle("Indicateurs Back Office - Période: " + 
                       date1.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                       " au " + date2.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Set legend position and make chart more readable
        chart1.setLegendVisible(true);
        chart1.setAnimated(false);
    }

    private void generateChart2() {
        chart2.getData().clear();
        
        // Configure the category axis
        xAxis2.setCategories(FXCollections.<String>observableArrayList());
        
        XYChart.Series<String, Number> objectifSeries = new XYChart.Series<>();
        objectifSeries.setName("Objectifs");
        
        XYChart.Series<String, Number> realiseSeries = new XYChart.Series<>();
        realiseSeries.setName("Réalisé");

        // Create categories list for proper axis configuration
        ObservableList<String> categories = FXCollections.observableArrayList();
        
        // Use a map to ensure unique short names for categories
        java.util.Map<String, Integer> nameCount = new java.util.HashMap<>();
        for (IndicateurRow row : table4Rows) {
            String baseShortName = shortenName(row.getIndicateur());
            String shortName = baseShortName;
            int count = nameCount.getOrDefault(baseShortName, 0);
            if (count > 0) {
            shortName = baseShortName + " (" + (count + 1) + ")";
            }
            nameCount.put(baseShortName, count + 1);

            categories.add(shortName);
            objectifSeries.getData().add(new XYChart.Data<>(shortName, row.getObjectif()));
            realiseSeries.getData().add(new XYChart.Data<>(shortName, row.getRealise()));
        }

        // Set categories to the axis
        xAxis2.setCategories(categories);
        xAxis2.setTickLabelRotation(-45); // Rotate labels to avoid overlap
        
        chart2.getData().addAll(objectifSeries, realiseSeries);
        chart2.setTitle("Indicateurs Front Office - Période: " + 
                       date1.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                       " au " + date2.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Set legend position and make chart more readable
        chart2.setLegendVisible(true);
        chart2.setAnimated(false);
    }

    private String shortenName(String fullName) {
        if (fullName.length() > 30) {
            return fullName.substring(0, 27) + "...";
        }
        return fullName;
    }

@FXML
public void generateRap(javafx.event.ActionEvent event) {
    // Prépare les remplacements
    Map<String, String> replacements = new HashMap<>();
    replacements.put("{date1}", date1.getValue() != null ? date1.getValue().toString() : "");
    replacements.put("{date2}", date2.getValue() != null ? date2.getValue().toString() : "");
    replacements.put("{date3}", date3.getValue() != null ? date3.getValue().toString() : "");



replacements.put("{commentaire}",rapportTextArea.getText() );
    // Charger le modèle Word (template.docx doit être dans le dossier resources ou accessible)
    try (InputStream is = getClass().getResourceAsStream("/templates/template_rapport_demarche.docx");
         XWPFDocument doc = new XWPFDocument(is)) {

        // Remplacer dans tous les paragraphes
        for (XWPFParagraph p : doc.getParagraphs()) {
            replaceInParagraph(p, replacements);
        }
        // Remplacer dans les tableaux
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, replacements);
                    }
                }
            }
        }
        // Utiliser FileChooser pour demander où sauvegarder le fichier
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Enregistrer le rapport");
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("Document Word (*.docx)", "*.docx")
        );
        fileChooser.setInitialFileName("Rapport Espace entreprise");
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/espace entreprise"));
        java.io.File file = fileChooser.showSaveDialog(bo.getScene().getWindow());

        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
            }
        } else {
            // Annulé par l'utilisateur
            return;
        }
        // Sauvegarder le fichier généré
        try (FileOutputStream out = new FileOutputStream("rapport_espace_entreprise.docx")) {
            doc.write(out);
        }
        // Optionnel : afficher un message de succès
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Rapport Word généré avec succès !");
        alert.showAndWait();

    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération du rapport Word.");
        alert.showAndWait();
    }
}

// Méthode utilitaire pour remplacer les placeholders dans un paragraphe
private void replaceInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
    for (XWPFRun run : paragraph.getRuns()) {
        String text = run.getText(0);
        if (text != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                if (text.contains(entry.getKey())) {
                    text = text.replace(entry.getKey(), entry.getValue());
                }
            }
            run.setText(text, 0);
        }
    }
}
  
    // Updated RapportRow class with setters
    public static class RapportRow {
        private String col1, col2, col5, col6;
        private Float col3, col4;
        
        public RapportRow(String col1, String col2, Float col3, Float col4, String col5, String col6) {
            this.col1 = col1; this.col2 = col2; this.col3 = col3;
            this.col4 = col4; this.col5 = col5; this.col6 = col6;
        }
        
        // Getters
        public String getCol1() { return col1; }
        public String getCol2() { return col2; }
        public Float getCol3() { return col3; }
        public Float getCol4() { return col4; }
        public String getCol5() { return col5; }
        public String getCol6() { return col6; }
        
        // Setters for editing
        public void setCol1(String col1) { this.col1 = col1; }
        public void setCol2(String col2) { this.col2 = col2; }
        public void setCol3(Float col3) { this.col3 = col3; }
        public void setCol4(Float col4) { this.col4 = col4; }
        public void setCol5(String col5) { this.col5 = col5; }
        public void setCol6(String col6) { this.col6 = col6; }
    }

    // Updated IndicateurRow class with setters
    public static class IndicateurRow {
        private String indicateur, pourcentage;
        private Float objectif, realise;
        
        public IndicateurRow(String indicateur, Float objectif, Float realise, String pourcentage) {
            this.indicateur = indicateur;
            this.objectif = objectif;
            this.realise = realise;
            this.pourcentage = pourcentage;
        }
        
        // Getters
        public String getIndicateur() { return indicateur; }
        public Float getObjectif() { return objectif; }
        public Float getRealise() { return realise; }
        public String getPourcentage() { return pourcentage; }
        
        // Setters for editing
        public void setIndicateur(String indicateur) { this.indicateur = indicateur; }
        public void setObjectif(Float objectif) { this.objectif = objectif; }
        public void setRealise(Float realise) { this.realise = realise; }
        public void setPourcentage(String pourcentage) { this.pourcentage = pourcentage; }
    }
}