package ccis.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.converter.DefaultStringConverter;
import org.apache.poi.xwpf.usermodel.*;

import java.awt.Desktop;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicateurPerformanceController {

    @FXML private TableView<IndicateurRow> tableView;
    @FXML private TableColumn<IndicateurRow, String> indicPerf;
    @FXML private TableColumn<IndicateurRow, String> description;
    @FXML private TableColumn<IndicateurRow, String> frequence;
    @FXML private TableColumn<IndicateurRow, String> ProposObj;
    @FXML private TableColumn<IndicateurRow, String> ValActuelle;

    private ObservableList<IndicateurRow> data;

    @FXML
    public void initialize() {
        // Setup columns
        indicPerf.setCellValueFactory(cell -> cell.getValue().indicPerfProperty());
        description.setCellValueFactory(cell -> cell.getValue().descriptionProperty());
        frequence.setCellValueFactory(cell -> cell.getValue().frequenceProperty());
        ProposObj.setCellValueFactory(cell -> cell.getValue().proposObjProperty());
        ValActuelle.setCellValueFactory(cell -> cell.getValue().valActuelleProperty());

        indicPerf.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        description.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        frequence.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        ProposObj.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        ValActuelle.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));

        // Edit commit handlers
        indicPerf.setOnEditCommit(e -> e.getRowValue().setIndicPerf(e.getNewValue()));
        description.setOnEditCommit(e -> e.getRowValue().setDescription(e.getNewValue()));
        frequence.setOnEditCommit(e -> e.getRowValue().setFrequence(e.getNewValue()));
        ProposObj.setOnEditCommit(e -> e.getRowValue().setProposObj(e.getNewValue()));
        ValActuelle.setOnEditCommit(e -> e.getRowValue().setValActuelle(e.getNewValue()));

        // Sample data (modifiable)
        data = FXCollections.observableArrayList(
                new IndicateurRow("Nombre de Prospections Nationales ", "Total de prospections nationales ", "", "", ""),
                new IndicateurRow("Satisfaction des Clients ", "Note moyenne de satisfaction des clients ", "", "", ""),
                 new IndicateurRow("Revenus Générés", "Total des revenus issus des activités de prospection ", "", "", ""),
                new IndicateurRow("Nombre de Partenariats Noués ", "Total des nouveaux partenariats établis ", "", "", ""),
                 new IndicateurRow("Nouveaux Marchés Explorés ", "Nombre de nouveaux marchés géographiques ou sectoriels explorés ", "", "", ""),
                new IndicateurRow("Feedback des Prospects ", "Commentaires et retours sur la prospection", "", "", "")
        
                );
        tableView.setItems(data);
    }

    @FXML
    private void generate() {
        // Préparer les remplacements pour les placeholders
        Map<String, String> replacements = new HashMap<>();
        List<IndicateurRow> rows = tableView.getItems();
        for (int i = 0; i < rows.size(); i++) {
            int idx = i + 1;
            IndicateurRow row = rows.get(i);
            replacements.put("{" + idx + "3}", row.getFrequence());
            replacements.put("{" + idx + "4}", row.getProposObj());
            replacements.put("{" + idx + "5}", row.getValActuelle());
        }

        // Charger le template Word
        try (InputStream is = getClass().getResourceAsStream("/templates/template_indicateurs_performance.docx");
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

            // Choisir où sauvegarder
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Enregistrer le rapport");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Document Word (*.docx)", "*.docx")
            );
            fileChooser.setInitialDirectory(new File("C:/fichiers application ccis/prospection"));
            fileChooser.setInitialFileName("IndicateurPerformance.docx");
            File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

            if (file != null) {
                try (FileOutputStream out = new FileOutputStream(file)) {
                    doc.write(out);
                }
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            }
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

    // --- Classe modèle ---
    public static class IndicateurRow {
        private final SimpleStringProperty indicPerf;
        private final SimpleStringProperty description;
        private final SimpleStringProperty frequence;
        private final SimpleStringProperty proposObj;
        private final SimpleStringProperty valActuelle;

        public IndicateurRow(String indicPerf, String description, String frequence, String proposObj, String valActuelle) {
            this.indicPerf = new SimpleStringProperty(indicPerf);
            this.description = new SimpleStringProperty(description);
            this.frequence = new SimpleStringProperty(frequence);
            this.proposObj = new SimpleStringProperty(proposObj);
            this.valActuelle = new SimpleStringProperty(valActuelle);
        }

        public String getIndicPerf() { return indicPerf.get(); }
        public void setIndicPerf(String value) { indicPerf.set(value); }
        public SimpleStringProperty indicPerfProperty() { return indicPerf; }

        public String getDescription() { return description.get(); }
        public void setDescription(String value) { description.set(value); }
        public SimpleStringProperty descriptionProperty() { return description; }

        public String getFrequence() { return frequence.get(); }
        public void setFrequence(String value) { frequence.set(value); }
        public SimpleStringProperty frequenceProperty() { return frequence; }

        public String getProposObj() { return proposObj.get(); }
        public void setProposObj(String value) { proposObj.set(value); }
        public SimpleStringProperty proposObjProperty() { return proposObj; }

        public String getValActuelle() { return valActuelle.get(); }
        public void setValActuelle(String value) { valActuelle.set(value); }
        public SimpleStringProperty valActuelleProperty() { return valActuelle; }
    }
}