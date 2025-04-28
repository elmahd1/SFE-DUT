package ccis.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ccis.models.DemarcheAdministratif;
import java.util.*;


 public class ESDController {

    @FXML private TableView<DemarcheAdministratif> dataTable;
    @FXML private TableColumn<DemarcheAdministratif, String> dateContact;
    @FXML private TableColumn<DemarcheAdministratif, String> heureContact;
    @FXML private ComboBox<CheckBox> columnsCombo;



    private final Map<String, TableColumn<DemarcheAdministratif, ?>> columnMap = new LinkedHashMap<>();

    @FXML
    public void initialize() {
        // Setup columns
        columnMap.put("dateContact", dateContact);
        columnMap.put("heureContact", heureContact);

        // Configure combo box
        List<CheckBox> checkBoxes = new ArrayList<>();
        columnMap.forEach((name, column) -> {
            CheckBox cb = new CheckBox(name);
            cb.setSelected(column.isVisible());
            checkBoxes.add(cb);
        });
        columnsCombo.setItems(FXCollections.observableArrayList(checkBoxes));

        // Load data
       loadData();
    }

    private void loadData() {
        dataTable.getItems().setAll();
    }

    @FXML
    public void applyColumnVisibility() {
        columnsCombo.getItems().forEach(cb -> {
            TableColumn<DemarcheAdministratif, ?> column = columnMap.get(cb.getText());
            if (column != null) {
                column.setVisible(cb.isSelected());
            }
        });
    }
}