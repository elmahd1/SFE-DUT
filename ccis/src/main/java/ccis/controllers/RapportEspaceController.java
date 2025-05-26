package ccis.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class RapportEspaceController {

    @FXML private DatePicker date1;
    @FXML private DatePicker date2;

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
    @FXML private XYChart.Series<String, Number> series1;
    @FXML private XYChart.Series<String, Number> series2;

    @FXML private BarChart<String, Number> chart2;
    @FXML private CategoryAxis xAxis2;
    @FXML private NumberAxis yAxis2;

    // Rapport text area & bouton
    @FXML private TextArea rapportTextArea;
    @FXML private Button btnGenerer;

    @FXML
    public void initialize() {
        date1.setValue(LocalDate.now());
        date2.setValue(LocalDate.now());

        // Table BO (Back Office)
        col1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol1()));
        col2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol2()));
        col3.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol3()).asObject());
        col4.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol4()).asObject());
        col5.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol5()));
        col6.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol6()));

        // Ajout des 4 lignes personnalisées (exemple)
        ObservableList<RapportRow> boRows = FXCollections.observableArrayList(
            new RapportRow("Nombre de programmes répertoriés", "Annuelle", 0f, 0f, "", ""),
            new RapportRow("Nombre de démarches administratives répertoriés", "Annuelle", 0f, 0f, "", ""),
            new RapportRow("Nombre d’entreprises répertoriés (Annuaire des entreprises)", "Annuelle", 0f, 0f, "", ""),
            new RapportRow("Nombre\r\n" + //
                                "d’administration s répertoriées (Répertoire des administrations)", "Annuelle", 0f, 0f, "", "")
        );
        bo.setItems(boRows);

        // Table 2
        col7.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIndicateur()));
        col8.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getObjectif()).asObject());
        col9.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getRealise()).asObject());
        col10.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPourcentage()));
    // Ajout des 4 lignes personnalisées (exemple)
        ObservableList<IndicateurRow> t2Rows = FXCollections.observableArrayList(
            new IndicateurRow("", 0f, 0f, "0%"),
            new IndicateurRow("", 0f, 0f, "0%"),
            new IndicateurRow("", 0f, 0f, "0%"),
            new IndicateurRow("", 0f, 0f, "0%")
        );
        table2.setItems(t2Rows);
        // Table FO
        col11.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol1()));
        col12.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol2()));
        col13.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol3()).asObject());
        col14.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getCol4()).asObject());
        col15.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol5()));
        col16.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCol6()));


        // Ajout des 4 lignes personnalisées (exemple)
        ObservableList<RapportRow> foRows = FXCollections.observableArrayList(
            new RapportRow("Nombre de\r\n" + //
                                "ressortissants accueilli", "Annuelle",  0f, 0f,"", ""),
            new RapportRow("Nombre de prestations\r\n" + //
                                "rendues pour les\r\n" + //
                                "programmes à\r\n" + //
                                "l’attention des\r\n" + //
                                "entreprises", "Annuelle",  0f, 0f, "",""),
            new RapportRow("Nombre de prestations\r\n" + //
                                "rendues pour les\r\n" + //
                                "démarches\r\n" + //
                                "administratives", "Annuelle",  0f, 0f, "",""),
            new RapportRow("Nombre de prestations\r\n" + //
                                "rendues pour\r\n" + //
                                "l’annuaire des\r\n" + //
                                "entreprises", "Annuelle",  0f, 0f, "",""),
            new RapportRow("Nombre de prestations\r\n" + //
                                "rendues pour le\r\n" + //
                                "répertoire des\r\n" + //
                                "administrations", "Annuelle",  0f, 0f, "", "")
        );
        fo.setItems(foRows);
        // Table 4
        col17.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIndicateur()));
        col18.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getObjectif()).asObject());
        col19.setCellValueFactory(data -> new javafx.beans.property.SimpleFloatProperty(data.getValue().getRealise()).asObject());
        col20.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPourcentage()));
        ObservableList<IndicateurRow> t4Rows = FXCollections.observableArrayList(
            new IndicateurRow("", 0f, 0f, "0%"),
            new IndicateurRow("", 0f, 0f, "0%"),
            new IndicateurRow("", 0f, 0f, "0%"),
            new IndicateurRow("", 0f, 0f, "0%")
        );
        table2.setItems(t4Rows);
        // Préparation des graphiques (à compléter selon tes données)
        // chart1.getData().addAll(series1, series2);
        // chart2.getData().addAll(...);

        // Bouton générer (exemple)
        btnGenerer.setOnAction(e -> rapportTextArea.setText("Rapport généré le " + LocalDate.now()));
    }

    // Modèle pour TableView bo et fo
    public static class RapportRow {
        private final String col1, col2, col5, col6;
        private final Float col3, col4;
        public RapportRow(String col1, String col2, Float col3, Float col4, String col5, String col6) {
            this.col1 = col1; this.col2 = col2; this.col3 = col3;
            this.col4 = col4; this.col5 = col5; this.col6 = col6;
        }
        public String getCol1() { return col1; }
        public String getCol2() { return col2; }
        public Float getCol3() { return col3; }
        public Float getCol4() { return col4; }
        public String getCol5() { return col5; }
        public String getCol6() { return col6; }
    }

    // Modèle pour TableView table2 et table4
    public static class IndicateurRow {
        private final String indicateur, pourcentage;
        private final Float objectif, realise;
        public IndicateurRow(String indicateur, Float objectif, Float realise, String pourcentage) {
            this.indicateur = indicateur;
            this.objectif = objectif;
            this.realise = realise;
            this.pourcentage = pourcentage;
        }
        public String getIndicateur() { return indicateur; }
        public Float getObjectif() { return objectif; }
        public Float getRealise() { return realise; }
        public String getPourcentage() { return pourcentage; }
    }
}