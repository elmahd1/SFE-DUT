package ccis.controllers;

import ccis.dao.DemarcheAdministratifDao;
import ccis.models.DemarcheAdministratif;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheDemarcheController {
    // Circular Charts
    @FXML
    private PieChart chart1;
    @FXML
    private PieChart chart3;

    // Bar Chart
    @FXML
    private BarChart<String, Number> chart2;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    // Tables
    @FXML
    private TableView<ObjetVisiteData> table1;
    @FXML
    private TableColumn<ObjetVisiteData, String> objetColumn;
    @FXML
    private TableColumn<ObjetVisiteData, Integer> totalColumn;
    @FXML
    private TableColumn<ObjetVisiteData, Double> percentageColumn;

    @FXML
    private TableView<ObjetVisiteDetailData> table2;
    @FXML
    private TableColumn<ObjetVisiteDetailData, String> objetVisiteColumn;
    @FXML
    private TableColumn<ObjetVisiteDetailData, Double> montantColumn;
    @FXML
    private TableColumn<ObjetVisiteDetailData, Double> delaiMoyenColumn;
    @FXML
    private TableColumn<ObjetVisiteDetailData, Double> pourcentageMontantColumn;
    @FXML
    private TableColumn<ObjetVisiteDetailData, Double> pourcentageDelaiColumn;

    @FXML
    private TableView<FormeJuridiqueData> table3;
    @FXML
    private TableColumn<FormeJuridiqueData, String> formeColumn;
    @FXML
    private TableColumn<FormeJuridiqueData, Integer> nombreVisiteColumn;
    @FXML
    private TableColumn<FormeJuridiqueData, Double> percentageFormeColumn;

    // Buttons
    @FXML
    private Button downloadChart1Button;
    @FXML
    private Button copyChart1Button;
    @FXML
    private Button downloadChart2Button;
    @FXML
    private Button copyChart2Button;
    @FXML
    private Button downloadChart3Button;
    @FXML
    private Button copyChart3Button;
    @FXML
    private Button copyTable1Button;
    @FXML
    private Button copyTable2Button;
    @FXML
    private Button copyTable3Button;

    private DemarcheAdministratifDao dao = new DemarcheAdministratifDao();

    // Data models for tables
    public static class ObjetVisiteData {
        private final String objet;
        private final int total;
        private final double percentage;

        public ObjetVisiteData(String objet, int total, double percentage) {
            this.objet = objet;
            this.total = total;
            this.percentage = percentage;
        }

        public String getObjet() { return objet; }
        public int getTotal() { return total; }
        public double getPercentage() { return percentage; }
    }

    public static class FormeJuridiqueData {
        private final String forme;
        private final int nombreVisite;
        private final double percentage;

        public FormeJuridiqueData(String forme, int nombreVisite, double percentage) {
            this.forme = forme;
            this.nombreVisite = nombreVisite;
            this.percentage = percentage;
        }

        public String getForme() { return forme; }
        public int getNombreVisite() { return nombreVisite; }
        public double getPercentage() { return percentage; }
    }

    public static class ObjetVisiteDetailData {
        private final String objetVisite;
        private final double montant;
        private final double delaiMoyen;
        private final double pourcentageMontant;
        private final double pourcentageDelai;

        public ObjetVisiteDetailData(String objetVisite, double montant, double delaiMoyen, 
                                     double pourcentageMontant, double pourcentageDelai) {
            this.objetVisite = objetVisite;
            this.montant = montant;
            this.delaiMoyen = delaiMoyen;
            this.pourcentageMontant = pourcentageMontant;
            this.pourcentageDelai = pourcentageDelai;
        }

        public String getObjetVisite() { return objetVisite; }
        public double getMontant() { return montant; }
        public double getDelaiMoyen() { return delaiMoyen; }
        public double getPourcentageMontant() { return pourcentageMontant; }
        public double getPourcentageDelai() { return pourcentageDelai; }
    }

    @FXML
    public void initialize() {
        // Initialize table columns
        setupTableColumns();
        
        // Load data
        loadData();
        File exportDir=new File("C:\\ccis documents\\demarche administratif");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
    }

    private void setupTableColumns() {
        // Table 1 columns
        objetColumn.setCellValueFactory(new PropertyValueFactory<>("objet"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        percentageColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        percentageColumn.setCellFactory(column -> new TableCell<ObjetVisiteData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", item));
                }
            }
        });

        // Table 2 columns
        objetVisiteColumn.setCellValueFactory(new PropertyValueFactory<>("objetVisite"));
        montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
        delaiMoyenColumn.setCellValueFactory(new PropertyValueFactory<>("delaiMoyen"));
        pourcentageMontantColumn.setCellValueFactory(new PropertyValueFactory<>("pourcentageMontant"));
        pourcentageDelaiColumn.setCellValueFactory(new PropertyValueFactory<>("pourcentageDelai"));
        
        // Format percentage columns in Table 2
        pourcentageMontantColumn.setCellFactory(column -> new TableCell<ObjetVisiteDetailData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", item));
                }
            }
        });
        
        pourcentageDelaiColumn.setCellFactory(column -> new TableCell<ObjetVisiteDetailData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", item));
                }
            }
        });

        // Table 3 columns
        formeColumn.setCellValueFactory(new PropertyValueFactory<>("forme"));
        nombreVisiteColumn.setCellValueFactory(new PropertyValueFactory<>("nombreVisite"));
        percentageFormeColumn.setCellValueFactory(new PropertyValueFactory<>("percentage"));
        percentageFormeColumn.setCellFactory(column -> new TableCell<FormeJuridiqueData, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f%%", item));
                }
            }
        });
    }
private void loadData() {
        List<DemarcheAdministratif> data = dao.getAllDemarches();
        int total = data.size();

        // Maps for data aggregation
        Map<String, Integer> objetCounts = new HashMap<>();
        Map<String, Integer> formeCounts = new HashMap<>();
        Map<String, Long> objetDelaiTotal = new HashMap<>();
        Map<String, Integer> objetDelaiCount = new HashMap<>();
        Map<String, Double> objetMontants = new HashMap<>();

        long totalMinutes = 0;
        double totalMontant = 0.0;

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        for (DemarcheAdministratif item : data) {
            // Count objetVisite
            String objet = item.getObjetVisite();
            objetCounts.put(objet, objetCounts.getOrDefault(objet, 0) + 1);

            // Count formeJuridique
            String forme = item.getFormeJuridique();
            formeCounts.put(forme, formeCounts.getOrDefault(forme, 0) + 1);

            // Calculate delay and montant
            try {
                String d1 = item.getDateDepot();
                String t1 = item.getHeureDepot();
                String d2 = item.getDateDelivrance();
                String t2 = item.getHeureDelivrance();
                double montant = item.getMontant();
                totalMontant += montant;
                objetMontants.put(objet, objetMontants.getOrDefault(objet, 0.0) + montant);
                
                if (d1 == null || t1 == null || d2 == null || t2 == null ||
                    d1.isEmpty() || t1.isEmpty() || d2.isEmpty() || t2.isEmpty()) {
                    long defaultMinutes = 120;
                    totalMinutes += defaultMinutes;
                    objetDelaiTotal.put(objet, objetDelaiTotal.getOrDefault(objet, 0L) + defaultMinutes);
                    objetDelaiCount.put(objet, objetDelaiCount.getOrDefault(objet, 0) + 1);
                    continue;
                }

                LocalDate date1 = LocalDate.parse(d1, dateFormat);
                LocalTime time1 = LocalTime.parse(t1, timeFormat);
                LocalDate date2 = LocalDate.parse(d2, dateFormat);
                LocalTime time2 = LocalTime.parse(t2, timeFormat);

                Duration duration = Duration.between(date1.atTime(time1), date2.atTime(time2));
                long minutes = Math.max(duration.toMinutes(), 0);
                totalMinutes += minutes;
                objetDelaiTotal.put(objet, objetDelaiTotal.getOrDefault(objet, 0L) + minutes);
                objetDelaiCount.put(objet, objetDelaiCount.getOrDefault(objet, 0) + 1);
            } catch (Exception e) {
                long defaultMinutes = 120;
                totalMinutes += defaultMinutes;
                objetDelaiTotal.put(objet, objetDelaiTotal.getOrDefault(objet, 0L) + defaultMinutes);
                objetDelaiCount.put(objet, objetDelaiCount.getOrDefault(objet, 0) + 1);
            }
        }

        // PieChart 1: Objet Visite
        chart1.getData().clear();
        chart1.setTitle("Répartition par Objet de Visite");
        
        // Create data for Table 1
        ObservableList<ObjetVisiteData> table1Data = FXCollections.observableArrayList();
        
        for (Map.Entry<String, Integer> entry : objetCounts.entrySet()) {
            double percent = (entry.getValue() * 100.0) / total;
            chart1.getData().add(new PieChart.Data(entry.getKey() + " (" + String.format("%.1f", percent) + "%)", entry.getValue()));
            
            // Add to table1
            table1Data.add(new ObjetVisiteData(entry.getKey(), entry.getValue(), percent));
        }
        
        // Set Table 1 data
        table1.setItems(table1Data);

        // PieChart 3: Forme Juridique
        chart3.getData().clear();
        chart3.setTitle("Répartition par Forme Juridique");
        
        // Create data for Table 3
        ObservableList<FormeJuridiqueData> table3Data = FXCollections.observableArrayList();
        
        for (Map.Entry<String, Integer> entry : formeCounts.entrySet()) {
            double percent = (entry.getValue() * 100.0) / total;
            chart3.getData().add(new PieChart.Data(entry.getKey() + " (" + String.format("%.1f", percent) + "%)", entry.getValue()));
            
            // Add to table3
            table3Data.add(new FormeJuridiqueData(entry.getKey(), entry.getValue(), percent));
        }
        
        // Set Table 3 data
        table3.setItems(table3Data);

        // BarChart 2: Délai moyen en heures by objet
        chart2.getData().clear();
        chart2.setTitle("Délai Moyen et Montant par Objet de Visite");

        XYChart.Series<String, Number> montantSeries = new XYChart.Series<>();
        montantSeries.setName("Montant Total (DH)");
        XYChart.Series<String, Number> delaiSeries = new XYChart.Series<>();
        delaiSeries.setName("Délai Moyen (heures)");
        
        // Create data for Table 2
        ObservableList<ObjetVisiteDetailData> table2Data = FXCollections.observableArrayList();
        
        // Add bars for each objet visite with their montant and average delay
        for (String objet : objetCounts.keySet()) {
            // Montant
            double montantTotal = objetMontants.getOrDefault(objet, 0.0);
            double pourcentageMontant = (montantTotal * 100.0) / totalMontant;
            montantSeries.getData().add(new XYChart.Data<>(objet, montantTotal));
        
            // Délai
            long delaiMinutes = objetDelaiTotal.getOrDefault(objet, 0L);
            int count = objetDelaiCount.getOrDefault(objet, 0);
            double delaiMoyenHeures = count > 0 ? (delaiMinutes / 60.0) / count : 0;
            double pourcentageDelai = (delaiMinutes * 100.0) / (totalMinutes > 0 ? totalMinutes : 1);
            delaiSeries.getData().add(new XYChart.Data<>(objet, delaiMoyenHeures));
            
            // Add to table2
            table2Data.add(new ObjetVisiteDetailData(objet, montantTotal, delaiMoyenHeures, 
                                                    pourcentageMontant, pourcentageDelai));
        }
        
        chart2.getData().addAll(montantSeries, delaiSeries);
        
        // Set Table 2 data
        table2.setItems(table2Data);
        
        // Set axes labels
        xAxis.setLabel("Objet de Visite");
        yAxis.setLabel("Valeur");
    }

    public void savePieChartAsPng(PieChart chart, String filename) {
    WritableImage image = chart.snapshot(new SnapshotParameters(), null);
    File file = new File(filename);
    try {
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        System.out.println("Chart saved to: " + file.getAbsolutePath());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    
public void savebarChartAsPng(BarChart<String, Number> chart, String filename) {
            WritableImage image = chart.snapshot(new SnapshotParameters(), null);
            File file = new File(filename);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                System.out.println("Chart saved to: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
@FXML
private void copyChart1(ActionEvent event) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            WritableImage image = chart1.snapshot(new SnapshotParameters(), null);
            content.putImage(image);
            clipboard.setContent(content);
        }
        @FXML
        private void copyChart2(ActionEvent event) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            WritableImage image = chart2.snapshot(new SnapshotParameters(), null);
            content.putImage(image);
            clipboard.setContent(content);
        }
        
        @FXML
        private void copyChart3(ActionEvent event) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            WritableImage image = chart3.snapshot(new SnapshotParameters(), null);
            content.putImage(image);
            clipboard.setContent(content);
        }

 String path = "C:/ccis documents/demarche administratif/";
       
@FXML
private void downloadChart1(ActionEvent event) {
    savePieChartAsPng(chart1, path+"graphe objet de visite.png");
}
@FXML
private void downloadChart2(ActionEvent event) {
    savebarChartAsPng(chart2, path+"graphe delai et montant moyen par objet de visite.png");
}
@FXML
private void downloadChart3(ActionEvent event) {
    savePieChartAsPng(chart3, path+"graphe forme juridique.png");
}
@FXML
private void downloadTable1(ActionEvent event) {
    exportTableToCSV(table1, path+"table objet de visite.csv");
}
@FXML
private void downloadTable2(ActionEvent event) {
    exportTableToCSV(table2, path+"table delai et montant moyens par objet de visite.csv");
}
@FXML
private void downloadTable3(ActionEvent event) {
    exportTableToCSV(table3, path+"table forme juridique.csv");
}
public <T> void exportTableToCSV(TableView<T> table, String filename) {
    File file = new File(filename);
    try (PrintWriter writer = new PrintWriter(file)) {
        // En-têtes de colonnes
        for (TableColumn<T, ?> column : table.getColumns()) {
            writer.print(column.getText() + ";");
        }
        writer.println();

        // Données des lignes
        for (T item : table.getItems()) {
            for (TableColumn<T, ?> column : table.getColumns()) {
                Object cellData = column.getCellObservableValue(item).getValue();
                writer.print((cellData != null ? cellData.toString() : "") + ";");
            }
            writer.println();
        }

        System.out.println("Table exported to: " + file.getAbsolutePath());
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}