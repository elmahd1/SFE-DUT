package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import java.time.LocalDate;
import org.apache.poi.xwpf.usermodel.*;
import java.io.*;
import java.util.*;
public class RapportDemarcheController {

    @FXML private DatePicker date1;
    @FXML private DatePicker date2;
    @FXML private DatePicker date3;

    // Table 1: Indicateurs de performance
    @FXML private TableView<IndicateurRow> table1;
    @FXML private TableColumn<IndicateurRow, String> col1;
    @FXML private TableColumn<IndicateurRow, Float> col2;
    @FXML private TableColumn<IndicateurRow, Float> col3;
    @FXML private TableColumn<IndicateurRow, String> col4;

    // Table 2: Nombre de ressortissants par objet de visite
    @FXML private TableView<SimpleRow> table2;
    @FXML private TableColumn<SimpleRow, String> col5;
    @FXML private TableColumn<SimpleRow, Integer> col6;

    // Table 3: Nombre de demandes acceptées par type de document
    @FXML private TableView<SimpleRow> table3;
    @FXML private TableColumn<SimpleRow, String> col7;
    @FXML private TableColumn<SimpleRow, Integer> col8;

    // Table 4: Etat de traitement des demandes (acceptées/rejetées)
    @FXML private TableView<SimpleRow> table4;
    @FXML private TableColumn<SimpleRow, String> col9;
    @FXML private TableColumn<SimpleRow, Integer> col10;

    // Table 5: Recettes générées
    @FXML private TableView<RecetteRow> table5;
    @FXML private TableColumn<RecetteRow, String> col11;
    @FXML private TableColumn<RecetteRow, Float> col12;

    // Charts
    @FXML private PieChart pieChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis1;
    @FXML private NumberAxis yAxis1;

    // Rapport text area 
    @FXML private TextArea rapportTextArea;

    // Data storage
    private ObservableList<IndicateurRow> table1Rows;
    private ObservableList<SimpleRow> table2Rows;
    private ObservableList<SimpleRow> table3Rows;
    private ObservableList<SimpleRow> table4Rows;
    private ObservableList<RecetteRow> table5Rows;

    @FXML private Label label1;

    @FXML
    public void initialize() {
        date1.setValue(LocalDate.now().minusMonths(1));
        date2.setValue(LocalDate.now());
        date3.setValue(LocalDate.now());

        setupTable1();
        setupTable2();
        setupTable3();
        setupTable4();
        setupTable5();
        setupCharts();
        updateTotalsAndPercentages();
    }

    private void setupTable1() {
        col1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIndicateur()));
        col2.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getObjectif()).asObject());
        col3.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getRealise()).asObject());
        col4.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTaux()));

        col1.setCellFactory(TextFieldTableCell.forTableColumn());
        col2.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col3.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        col4.setCellFactory(TextFieldTableCell.forTableColumn());

        // Edit commit handlers
        col1.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setIndicateur(event.getNewValue());
        });
        col2.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setObjectif(event.getNewValue());
            updateTaux(row);
            updateTotalsAndPercentages();
        });
        col3.setOnEditCommit(event -> {
            IndicateurRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setRealise(event.getNewValue());
            updateTaux(row);
            updateTotalsAndPercentages();
        });

        table1Rows = FXCollections.observableArrayList(
            new IndicateurRow("Nombre de ressortissants accueillis", 0f, 0f, "0%"),
            new IndicateurRow("Nombre de documents administratifs délivrés", 0f, 0f, "0%"),
            new IndicateurRow("Recettes générées en DH", 0f, 0f, "0%"),
            new IndicateurRow("Taux de satisfaction", 0f, 0f, "0%")
        );
        table1.setItems(table1Rows);
    }

    private void setupTable2() {
        col5.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLabel()));
        col6.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getValue()).asObject());

        col5.setCellFactory(TextFieldTableCell.forTableColumn());
        col6.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        col5.setOnEditCommit(event -> {
            SimpleRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setLabel(event.getNewValue());
        });
        col6.setOnEditCommit(event -> {
            SimpleRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setValue(event.getNewValue());
            updateTotalsAndPercentages();
        });

        table2Rows = FXCollections.observableArrayList(
            new SimpleRow("Demande de document administratif", 0),
            new SimpleRow("Demande d’information /renseignement", 0),
            new SimpleRow("Total général", 0)
        );
        table2.setItems(table2Rows);
    }

    private void setupTable3() {
        col7.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLabel()));
        col8.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getValue()).asObject());

        col7.setCellFactory(TextFieldTableCell.forTableColumn());
        col8.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        col7.setOnEditCommit(event -> {
            SimpleRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setLabel(event.getNewValue());
        });
        col8.setOnEditCommit(event -> {
            SimpleRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setValue(event.getNewValue());
            updateTotalsAndPercentages();
        });

        table3Rows = FXCollections.observableArrayList(
            new SimpleRow("Carte professionnelle", 0),
            new SimpleRow("Certificat d'origine", 0),
            new SimpleRow("Attestation professionnelle", 0),
            new SimpleRow("Visa des factures", 0),
            new SimpleRow("Visa des documents commerciaux", 0),
            new SimpleRow("Total général", 0)
        );
        table3.setItems(table3Rows);
    }

    private void setupTable4() {
        col9.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLabel()));
        col10.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getValue()).asObject());

        col9.setCellFactory(TextFieldTableCell.forTableColumn());
        col10.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        col9.setOnEditCommit(event -> {
            SimpleRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setLabel(event.getNewValue());
        });
        col10.setOnEditCommit(event -> {
            SimpleRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setValue(event.getNewValue());
            updateTotalsAndPercentages();
        });

        table4Rows = FXCollections.observableArrayList(
            new SimpleRow("Acceptées", 0),
            new SimpleRow("Rejetées", 0),
            new SimpleRow("Total", 0)
        );
        table4.setItems(table4Rows);
    }

    private void setupTable5() {
        col11.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDocument()));
        col12.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getRecette()).asObject());

        col11.setCellFactory(TextFieldTableCell.forTableColumn());
        col12.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        col11.setOnEditCommit(event -> {
            RecetteRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setDocument(event.getNewValue());
        });
        col12.setOnEditCommit(event -> {
            RecetteRow row = event.getTableView().getItems().get(event.getTablePosition().getRow());
            row.setRecette(event.getNewValue());
            updateTotalsAndPercentages();
        });

        table5Rows = FXCollections.observableArrayList(
            new RecetteRow("Carte professionnelle", 0f),
            new RecetteRow("Certificat d'origine", 0f),
            new RecetteRow("Attestation professionnelle", 0f),
            new RecetteRow("Visa des factures", 0f),
            new RecetteRow("Visa des documents commerciaux", 0f),
            new RecetteRow("Total", 0f)
        );
        table5.setItems(table5Rows);
    }

    private void setupCharts() {
        // PieChart for table4 (acceptées/rejetées)
        // Get values from table4Rows for "Acceptées" and "Rejetées"
        // PieChart for table3 (nombre de demandes acceptées par type de document)
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (SimpleRow row : table3Rows) {
            if (!row.getLabel().equalsIgnoreCase("Total général")) {
            pieChartData.add(new PieChart.Data(row.getLabel() + ": " + row.getValue(), row.getValue()));
            }
        }
        pieChart.setData(pieChartData);

        // BarChart for table5 (recettes)
        barChart.getData().clear();
        xAxis1.setLabel("Document administratif");
        yAxis1.setLabel("Somme de Recette en dh");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (RecetteRow row : table5Rows) {
            if (!row.getDocument().equalsIgnoreCase("Total")) {
                series.getData().add(new XYChart.Data<>(row.getDocument(), row.getRecette()));
            }
        }
        barChart.getData().add(series);
    }




    // --- Totals and Percentages ---
    private void updateTotalsAndPercentages() {
        // Table 1: update taux
        for (IndicateurRow row : table1Rows) {
            updateTaux(row);
        }
        table1.refresh();

        // Table 2: total général
        if (table2Rows.size() >= 3) {
            int total = 0;
            for (int i = 0; i < table2Rows.size() - 1; i++) {
                total += table2Rows.get(i).getValue();
            }
            table2Rows.get(table2Rows.size() - 1).setValue(total);
        }
        table2.refresh();

        // Table 3: total général
        if (table3Rows.size() >= 3) {
            int total = 0;
            for (int i = 0; i < table3Rows.size() - 1; i++) {
                total += table3Rows.get(i).getValue();
            }
            table3Rows.get(table3Rows.size() - 1).setValue(total);
        }
        table3.refresh();

        // Table 4: total
        if (table4Rows.size() >= 3) {
            int total = 0;
            for (int i = 0; i < table4Rows.size() - 1; i++) {
                total += table4Rows.get(i).getValue();
            }
            table4Rows.get(table4Rows.size() - 1).setValue(total);
        }
        table4.refresh();

        // Table 5: total
        if (table5Rows.size() >= 3) {
            float total = 0f;
            for (int i = 0; i < table5Rows.size() - 1; i++) {
                total += table5Rows.get(i).getRecette();
            }
            table5Rows.get(table5Rows.size() - 1).setRecette(total);
        }
        table5.refresh();

        // Update charts
        setupCharts();
    }

    private void updateTaux(IndicateurRow row) {
        if (row.getObjectif() != 0) {
            float taux = (row.getRealise() / row.getObjectif()) * 100;
            row.setTaux(String.format("%.1f%%", taux));
        } else {
            row.setTaux("N/A");
        }
    }



@FXML
public void generateRap(javafx.event.ActionEvent event) {
    // Prépare les remplacements
    Map<String, String> replacements = new HashMap<>();
    replacements.put("{date1}", date1.getValue() != null ? date1.getValue().toString() : "");
    replacements.put("{date2}", date2.getValue() != null ? date2.getValue().toString() : "");
    replacements.put("{date3}", date3.getValue() != null ? date3.getValue().toString() : "");

replacements.put("{label1}", label1+" ("+date1 +"," + date2 +").");

// Table 1 replacements: {11}, {12}, {13}, {14}, {21}, {22}, ...
for (int i = 0; i < table1Rows.size(); i++) {
    IndicateurRow row = table1Rows.get(i);
    replacements.put("{1" + (i + 1) + "2}", row.getObjectif() != null ? row.getObjectif().toString() : "");
    replacements.put("{1" + (i + 1) + "3}", row.getRealise() != null ? row.getRealise().toString() : "");
    replacements.put("{1" + (i + 1) + "4}", row.getTaux() != null ? row.getTaux() : "");
}

// Table 2 replacements: {51}, {52}, {61}, {62}, ...
for (int i = 0; i < table2Rows.size(); i++) {
    SimpleRow row = table2Rows.get(i);
    replacements.put("{2" + (i + 5) + "2}", row.getValue() != null ? row.getValue().toString() : "");
}

// Table 3 replacements: {71}, {72}, {81}, {82}, ...
for (int i = 0; i < table3Rows.size(); i++) {
    SimpleRow row = table3Rows.get(i);
    replacements.put("{3" + (i + 7) + "2}", row.getValue() != null ? row.getValue().toString() : "");
}

// Table 4 replacements: {91}, {92}, {101}, {102}, ...
for (int i = 0; i < table4Rows.size(); i++) {
    SimpleRow row = table4Rows.get(i);
    replacements.put("{4" + (i + 9) + "2}", row.getValue() != null ? row.getValue().toString() : "");
}

// Table 5 replacements: {111}, {112}, {121}, {122}, ...
for (int i = 0; i < table5Rows.size(); i++) {
    RecetteRow row = table5Rows.get(i);
    replacements.put("{5" + (i + 11) + "2}", row.getRecette() != null ? row.getRecette().toString() : "");
}
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
        fileChooser.setInitialFileName("Rapport demarche administrative");
        fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/demarche administrative"));
        java.io.File file = fileChooser.showSaveDialog(table1.getScene().getWindow());

        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file)) {
            doc.write(out);
            }
        } else {
            // Annulé par l'utilisateur
            return;
        }
        // Sauvegarder le fichier généré
        try (FileOutputStream out = new FileOutputStream("rapport_demarches.docx")) {
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
    // --- Models ---
    public static class IndicateurRow {
        private String indicateur, taux;
        private Float objectif, realise;
        public IndicateurRow(String indicateur, Float objectif, Float realise, String taux) {
            this.indicateur = indicateur;
            this.objectif = objectif;
            this.realise = realise;
            this.taux = taux;
        }
        public String getIndicateur() { return indicateur; }
        public Float getObjectif() { return objectif; }
        public Float getRealise() { return realise; }
        public String getTaux() { return taux; }
        public void setIndicateur(String indicateur) { this.indicateur = indicateur; }
        public void setObjectif(Float objectif) { this.objectif = objectif; }
        public void setRealise(Float realise) { this.realise = realise; }
        public void setTaux(String taux) { this.taux = taux; }
    }

    public static class SimpleRow {
        private String label;
        private Integer value;
        public SimpleRow(String label, Integer value) {
            this.label = label;
            this.value = value;
        }
        public String getLabel() { return label; }
        public Integer getValue() { return value; }
        public void setLabel(String label) { this.label = label; }
        public void setValue(Integer value) { this.value = value; }
    }

    public static class RecetteRow {
        private String document;
        private Float recette;
        public RecetteRow(String document, Float recette) {
            this.document = document;
            this.recette = recette;
        }
        public String getDocument() { return document; }
        public Float getRecette() { return recette; }
        public void setDocument(String document) { this.document = document; }
        public void setRecette(Float recette) { this.recette = recette; }
    }
}