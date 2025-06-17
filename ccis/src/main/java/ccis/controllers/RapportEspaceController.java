package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.WritableImage;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.Desktop;
import java.io.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import ccis.models.EspaceEntreprise;
import ccis.dao.EspaceEntrepriseDAO;
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
    @FXML private TableColumn<RapportRow, Integer> col3;
    @FXML private TableColumn<RapportRow, Integer> col4;
    @FXML private TableColumn<RapportRow, Integer> col5;
    @FXML private TableColumn<RapportRow, String> col6;

    // Table 2
    @FXML private TableView<IndicateurRow> table2;
    @FXML private TableColumn<IndicateurRow, String> col7;
    @FXML private TableColumn<IndicateurRow, Integer> col8;
    @FXML private TableColumn<IndicateurRow, Integer> col9;
    @FXML private TableColumn<IndicateurRow, String> col10;

    // Table FO
    @FXML private TableView<RapportRow> fo;
    @FXML private TableColumn<RapportRow, String> col11;
    @FXML private TableColumn<RapportRow, String> col12;
    @FXML private TableColumn<RapportRow, Integer> col13;
    @FXML private TableColumn<RapportRow, Integer> col14;
    @FXML private TableColumn<RapportRow, Integer> col15;
    @FXML private TableColumn<RapportRow, String> col16;

    // Table 4
    @FXML private TableView<IndicateurRow> table4;
    @FXML private TableColumn<IndicateurRow, String> col17;
    @FXML private TableColumn<IndicateurRow, Integer> col18;
    @FXML private TableColumn<IndicateurRow, Integer> col19;
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

    // Data storage
    private ObservableList<RapportRow> boRows;
    private ObservableList<IndicateurRow> table2Rows;
    private ObservableList<RapportRow> foRows;
    private ObservableList<IndicateurRow> table4Rows;

    private EspaceEntrepriseDAO espaceEntrepriseDAO = new EspaceEntrepriseDAO();
    @FXML
    public void initialize() {
        date1.setValue(LocalDate.now().minusMonths(1));
        date2.setValue(LocalDate.now());

        setupBackOfficeTable();
        setupTable2();
        setupFrontOfficeTable();
        setupTable4();
        
generateChart1();
generateChart2();
    }

    private void setupBackOfficeTable() {
        // Set cell value factories
        col1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol1()));
        col2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol2()));
        col3.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCol3()).asObject());
        col4.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCol4()).asObject());
        col5.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCol5()).asObject());
        col6.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol6()));

        // Make columns editable
        col1.setCellFactory(TextFieldTableCell.forTableColumn());
        col2.setCellFactory(TextFieldTableCell.forTableColumn());
        col3.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col4.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col5.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
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
            new RapportRow("Nombre de programmes répertoriés", "Annuelle", 0, 0, 0, "Coordonnateur ; Équipe EE"),
            new RapportRow("Nombre de démarches administratives répertoriés", "Annuelle", 0, 0, 0, "Coordonnateur ; Équipe EE"),
            new RapportRow("Nombre d'entreprises répertoriés (Annuaire des entreprises)", "Annuelle", 0, 0, 0, "Coordonnateur ; Le responsable\r\n" + //
                                "consolidation\r\n" + //
                                "annuaire+ équipe EE"),
            new RapportRow("Nombre d'administrations répertoriées (Répertoire des administrations)", "Annuelle", 0, 0, 0, "Coordonnateur ; Le responsable\r\n" + //
                                "consolidation\r\n" + //
                                "annuaire+ équipe EE")
        );
        bo.setItems(boRows);
    }

    private void setupTable2() {
        // Set cell value factories
        col7.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIndicateur()));
        col8.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getObjectif()).asObject());
        col9.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRealise()).asObject());
        col10.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPourcentage()));

        // Make columns editable
        col7.setCellFactory(TextFieldTableCell.forTableColumn());
        col8.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col9.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col10.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set edit commit handlers
        col7.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setIndicateur(event.getNewValue());
        });
        col8.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setObjectif(event.getNewValue().intValue());
            updatePercentage(row);
            table2.refresh();
        });
        col9.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setRealise(event.getNewValue().intValue());
            updatePercentage(row);
            table2.refresh();
        });

        // Initialize data - FIXED: was setting to table2 instead of table4
        // Récupérer les valeurs pour la 3ème colonne (Objectif) depuis la DAO ou une méthode de comptage
        int nbProgrammes = espaceEntrepriseDAO.countByType("Programmes d'appui / aide aux entreprises");
        int nbDemarches = espaceEntrepriseDAO.countByType("Demarches administratives");
        int nbAnnuaire = espaceEntrepriseDAO.countByType("Annuaire des entreprises");
        int nbRepertoire = espaceEntrepriseDAO.countByType("Repertoire de contact des administrations");
        int somme = espaceEntrepriseDAO.count(); // Total des entreprises

        table2Rows = FXCollections.observableArrayList(
            new IndicateurRow("Programmes d'appui / aide aux entreprises", 0, nbProgrammes, "0%"),
            new IndicateurRow("Demarches administratives", 0, nbDemarches, "0%"),
            new IndicateurRow("Annuaire des entreprises", 0, nbAnnuaire, "0%"),
            new IndicateurRow("Repertoire de contact des administrations", 0, nbRepertoire, "0%")
        );
        table2Rows.add(new IndicateurRow("Total", 0, somme, "0%"));
        table2.setItems(table2Rows);
    }

    private void setupFrontOfficeTable() {
        // Set cell value factories
        col11.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol1()));
        col12.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol2()));
        col13.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCol3()).asObject());
        col14.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCol4()).asObject());
        col15.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCol5()).asObject());
        col16.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol6()));

        // Make columns editable
        col11.setCellFactory(TextFieldTableCell.forTableColumn());
        col12.setCellFactory(TextFieldTableCell.forTableColumn());
        col13.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col14.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col15.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
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
        // Initialize data - FIXED: was setting to table2 instead of table4
        // Récupérer les valeurs pour la 3ème colonne (Objectif) depuis la DAO ou une méthode de comptage
        int nbProgrammes = espaceEntrepriseDAO.countByType("Programmes d'appui / aide aux entreprises");
        int nbDemarches = espaceEntrepriseDAO.countByType("Demarches administratives");
        int nbAnnuaire = espaceEntrepriseDAO.countByType("Annuaire des entreprises");
        int nbRepertoire = espaceEntrepriseDAO.countByType("Repertoire de contact des administrations");
        int somme = espaceEntrepriseDAO.count(); // Total des entreprises
        // Initialize data
        foRows = FXCollections.observableArrayList(

            new RapportRow("Nombre de prestations rendues pour les programmes à l'attention des entreprises", "Annuelle", 0, 0, nbProgrammes,  "Équipe EE"),
            new RapportRow("Nombre de prestations rendues pour les démarches administratives", "Annuelle", 0, 0, nbDemarches,  "Équipe EE"),
            new RapportRow("Nombre de prestations rendues pour l'annuaire des entreprises", "Annuelle", 0, 0, nbAnnuaire, "Le responsable responsable consolidation annuaire, équipe EE"),
            new RapportRow("Nombre de prestations rendues pour le répertoire des administrations", "Annuelle", 0, 0, nbRepertoire, "Le responsable responsable consolidation répertoire, équipe EE"),
            new RapportRow("Nombre de ressortissants accueillis", "Annuelle", 0, 0, somme, "Équipe EE")
        );
        fo.setItems(foRows);
    }

    private void setupTable4() {
        // Set cell value factories
        col17.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIndicateur()));
        col18.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getObjectif()).asObject());
        col19.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getRealise()).asObject());
        col20.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPourcentage()));

        // Make columns editable
        col17.setCellFactory(TextFieldTableCell.forTableColumn());
        col18.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col19.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col20.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set edit commit handlers
        col17.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setIndicateur(event.getNewValue());
        });
        col18.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setObjectif(event.getNewValue().intValue());
            updatePercentage(row);
            table4.refresh();
        });
        col19.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setRealise(event.getNewValue().intValue());
            updatePercentage(row);
            table4.refresh();
        });

        // Récupérer les valeurs pour la 3ème colonne (réalisé) depuis la DAO ou une méthode de comptage
        int nbPP = espaceEntrepriseDAO.countByFormeJuridique("PP (Personne physique)");
        int nbAutoEntrepreneur = espaceEntrepriseDAO.countByFormeJuridique("Auto-entrepreneur");
        int nbSARL = espaceEntrepriseDAO.countByFormeJuridique("SARL");
        int nbSA = espaceEntrepriseDAO.countByFormeJuridique("SA");
        int somme = espaceEntrepriseDAO.count(); // Total des entreprises

        table4Rows = FXCollections.observableArrayList(

            new IndicateurRow("PP (Personne physique)", 0, nbPP, "0%"),
            new IndicateurRow("Auto-entrepreneur", 0, nbAutoEntrepreneur, "0%"),
            new IndicateurRow("SARL", 0, nbSARL, "0%"),
            new IndicateurRow("SA", 0, nbSA, "0%")
  
        );
        table4Rows.add(new IndicateurRow("Total", 0, somme, "0%"));
        table4.setItems(table4Rows);
    }


    private void updatePercentage(IndicateurRow row) {
        if (row.getObjectif() != 0) {
            float percentage = ((float) row.getRealise() / (float) row.getObjectif()) * 100;
            row.setPourcentage(String.format("%.1f%%", percentage));
        } else {
            row.setPourcentage("N/A");
        }
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
        realiseSeries.setName("Réalisations");

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
        chart1.setTitle("Indicateurs Front Office - Période: " + 
                       date1.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                       " au " + date2.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "(Activité)");
        
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
                       " au " + date2.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" (forme juridique)");
        
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
    generateChart1();
    generateChart2();
    
    // Prépare les remplacements
    Map<String, String> replacements = new HashMap<>();
    replacements.put("{date1}", date1.getValue() != null ? date1.getValue().toString() : "");
    replacements.put("{date2}", date2.getValue() != null ? date2.getValue().toString() : "");
    replacements.put("{date3}", date3.getValue() != null ? date3.getValue().toString() : "");

    // Remplir les valeurs pour boRows (Back Office)
    for (int i = 0; i < boRows.size(); i++) {
        RapportRow row = boRows.get(i);
        replacements.put("{1" + (i + 1) + "1}", row.getCol1() != null ? row.getCol1() : "");
        replacements.put("{1" + (i + 1) + "2}", row.getCol2() != null ? row.getCol2() : "");
        replacements.put("{1" + (i + 1) + "3}", row.getCol3() != 0 ? String.valueOf(row.getCol3()) : "");
        replacements.put("{1" + (i + 1) + "4}", row.getCol4() != 0 ? String.valueOf(row.getCol4()) : "");
        replacements.put("{1" + (i + 1) + "5}", row.getCol5() != 0 ? String.valueOf(row.getCol5()) : "");
        replacements.put("{1" + (i + 1) + "6}", row.getCol6() != null ? row.getCol6() : "");
    }

    // Remplir les valeurs pour table2Rows (Indicateurs BO)
    for (int i = 0; i < table2Rows.size(); i++) {
        IndicateurRow row = table2Rows.get(i);
        replacements.put("{2" + (i + 1) + "1}", row.getIndicateur() != null ? row.getIndicateur() : "");
        replacements.put("{2" + (i + 1) + "2}", row.getObjectif() != 0 ? String.valueOf(row.getObjectif()) : "");
        replacements.put("{2" + (i + 1) + "3}", row.getRealise() != 0 ? String.valueOf(row.getRealise()) : "");
        replacements.put("{2" + (i + 1) + "4}", row.getPourcentage() != null ? row.getPourcentage() : "");
    }
// Remplir les valeurs pour foRows (Front Office)
for (int i = 0; i < foRows.size(); i++) {
    RapportRow row = foRows.get(i);
    int base = 3; // 3ème tableau
    int idx = i + 1;
    replacements.put("{" + base + idx + "1}", row.getCol1() != null ? row.getCol1() : ""); // Indicateur
    replacements.put("{" + base + idx + "2}", row.getCol2() != null ? row.getCol2() : ""); // Fréquence
    replacements.put("{" + base + idx + "3}", row.getCol3() != 0 ? String.valueOf(row.getCol3()) : ""); // Objectif
    replacements.put("{" + base + idx + "4}", row.getCol4() != 0 ? String.valueOf(row.getCol4()) : ""); // n-1
    replacements.put("{" + base + idx + "5}", row.getCol5() != 0 ? String.valueOf(row.getCol5()) : ""); // Tolérance
    replacements.put("{" + base + idx + "6}", row.getCol6() != null ? row.getCol6() : ""); // Méthode de calcul ou Responsabilité
}

    // Remplir les valeurs pour table4Rows (Indicateurs FO)
    for (int i = 0; i < table4Rows.size(); i++) {
        IndicateurRow row = table4Rows.get(i);
        replacements.put("{4" + (i + 1) + "1}", row.getIndicateur() != null ? row.getIndicateur() : "");
        replacements.put("{4" + (i + 1) + "2}", row.getObjectif() != 0 ? String.valueOf(row.getObjectif()) : "");
        replacements.put("{4" + (i + 1) + "3}", row.getRealise() != 0 ? String.valueOf(row.getRealise()) : "");
        replacements.put("{4" + (i + 1) + "4}", row.getPourcentage() != null ? row.getPourcentage() : "");
    }

    replacements.put("{commentaire}", rapportTextArea.getText());

    // Charger le modèle Word (template.docx doit être dans le dossier resources ou accessible)
    try (InputStream is = getClass().getResourceAsStream("/templates/template_rapport_espace.docx");
         XWPFDocument doc = new XWPFDocument(is)) {

        // Create temporary image files for charts
        File barChart1File = null;
        File barChart2File = null;
        
        try {
            // Generate chart images
            WritableImage barImage1 = getBarChartImage(chart1);
            WritableImage barImage2 = getBarChartImage(chart2);
            
            // Create temporary files
            barChart1File = File.createTempFile("bar_chart1_", ".png");
            barChart2File = File.createTempFile("bar_chart2_", ".png");
            
            // Save images to temporary files
            javax.imageio.ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(barImage1, null), "png", barChart1File);
            javax.imageio.ImageIO.write(javafx.embed.swing.SwingFXUtils.fromFXImage(barImage2, null), "png", barChart2File);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Remplacer dans tous les paragraphes
        for (XWPFParagraph p : doc.getParagraphs()) {
            replaceInParagraph(p, replacements);
            // Insert charts if placeholders found
            insertChartsInParagraph(p, barChart1File, barChart2File);
        }
        
        // Remplacer dans les tableaux
        for (XWPFTable table : doc.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph p : cell.getParagraphs()) {
                        replaceInParagraph(p, replacements);
                        // Insert charts if placeholders found
                        insertChartsInParagraph(p, barChart1File, barChart2File);
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
        
        // Clean up temporary files
        if (barChart1File != null && barChart1File.exists()) {
            barChart1File.delete();
        }
        if (barChart2File != null && barChart2File.exists()) {
            barChart2File.delete();
        }
        
        // Optionnel : afficher un message de succès
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(file);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Le document a été généré avec succès.");
            alert.showAndWait();
        }

    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur lors de la génération du rapport Word.");
        alert.showAndWait();
    }
}

// New method to insert charts in paragraphs for RapportEspaceController
private void insertChartsInParagraph(XWPFParagraph paragraph, File barChart1File, File barChart2File) {
    String text = paragraph.getText();
    if (text != null) {
        if (text.contains("{barChart1}") && barChart1File != null) {
            // Clear existing text and insert bar chart 1
            while (paragraph.getRuns().size() > 0) {
                paragraph.removeRun(0);
            }
            XWPFRun run = paragraph.createRun();
            try (FileInputStream fis = new FileInputStream(barChart1File)) {
                run.addPicture(fis, XWPFDocument.PICTURE_TYPE_PNG, "barChart1.png", 
                              Units.toEMU(400), Units.toEMU(300)); // Adjust size as needed
            } catch (Exception e) {
                e.printStackTrace();
                run.setText("Erreur lors de l'insertion du graphique Back Office");
            }
        } else if (text.contains("{barChart2}") && barChart2File != null) {
            // Clear existing text and insert bar chart 2
            while (paragraph.getRuns().size() > 0) {
                paragraph.removeRun(0);
            }
            XWPFRun run = paragraph.createRun();
            try (FileInputStream fis = new FileInputStream(barChart2File)) {
                run.addPicture(fis, XWPFDocument.PICTURE_TYPE_PNG, "barChart2.png", 
                              Units.toEMU(400), Units.toEMU(300)); // Adjust size as needed
            } catch (Exception e) {
                e.printStackTrace();
                run.setText("Erreur lors de l'insertion du graphique Front Office");
            }
        }
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
public WritableImage getBarChartImage(BarChart<String, Number> chart) {
    return chart.snapshot(new SnapshotParameters(), null);
}



    // Updated RapportRow class with setters
    public static class RapportRow {
        private String col1, col2, col6;
        private int col3, col4 , col5;
        
        public RapportRow(String col1, String col2, int col3, int col4, int col5, String col6) {
            this.col1 = col1; this.col2 = col2; this.col3 = col3;
            this.col4 = col4; this.col5 = col5; this.col6 = col6;
        }
        
        // Getters
        public String getCol1() { return col1; }
        public String getCol2() { return col2; }
        public int getCol3() { return col3; }
        public int getCol4() { return col4; }
        public int getCol5() { return col5; }
        public String getCol6() { return col6; }
        
        // Setters for editing
        public void setCol1(String col1) { this.col1 = col1; }
        public void setCol2(String col2) { this.col2 = col2; }
        public void setCol3(int col3) { this.col3 = col3; }
        public void setCol4(int col4) { this.col4 = col4; }
        public void setCol5(int col5) { this.col5 = col5; }
        public void setCol6(String col6) { this.col6 = col6; }
    }

    // Updated IndicateurRow class with setters
    public static class IndicateurRow {
        private String indicateur, pourcentage;
        private int objectif, realise;
        
        public IndicateurRow(String indicateur, int objectif, int realise, String pourcentage) {
            this.indicateur = indicateur;
            this.objectif = objectif;
            this.realise = realise;
            this.pourcentage = pourcentage;
        }
        
        // Getters
        public String getIndicateur() { return indicateur; }
        public int getObjectif() { return objectif; }
        public int getRealise() { return realise; }
        public String getPourcentage() { return pourcentage; }
        
        // Setters for editing
        public void setIndicateur(String indicateur) { this.indicateur = indicateur; }
        public void setObjectif(int objectif) { this.objectif = objectif; }
        public void setRealise(int realise) { this.realise = realise; }
        public void setPourcentage(String pourcentage) { this.pourcentage = pourcentage; }
    }
}