package com.clubmanagement.view;

import com.clubmanagement.dao.ClubDAO;
import com.clubmanagement.model.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class ClubView {
    private VBox view;
    private TableView<Club> table;
    private ClubDAO dao;
    private ObservableList<Club> data;

    private TextField txtName, txtDesc, txtAdvisor, txtCapacity;
    private DatePicker dpFoundation;

    public ClubView() {
        dao = new ClubDAO();
        view = new VBox(20);
        view.setPadding(new Insets(15));
        
        Label title = new Label("Kulüp Yönetimi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        setupTable();
        setupForm();

        view.getChildren().addAll(title, table, createFormBox());
        loadData();
    }

    private void setupTable() {
        table = new TableView<>();

        TableColumn<Club, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Club, String> colName = new TableColumn<>("Kulüp Adı");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setPrefWidth(150);

        TableColumn<Club, String> colDesc = new TableColumn<>("Açıklama");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDesc.setPrefWidth(200);

        TableColumn<Club, String> colDate = new TableColumn<>("Kuruluş Tarihi");
        colDate.setCellValueFactory(new PropertyValueFactory<>("foundationDate"));

        TableColumn<Club, String> colAdvisor = new TableColumn<>("Danışman Hoca");
        colAdvisor.setCellValueFactory(new PropertyValueFactory<>("advisorName"));
        colAdvisor.setPrefWidth(150);

        TableColumn<Club, Integer> colCap = new TableColumn<>("Kontenjan");
        colCap.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        
        TableColumn<Club, Integer> colMemCount = new TableColumn<>("Mevcut Üye");
        colMemCount.setCellValueFactory(new PropertyValueFactory<>("currentMemberCount"));

        table.getColumns().add(colId);
        table.getColumns().add(colName);
        table.getColumns().add(colDesc);
        table.getColumns().add(colDate);
        table.getColumns().add(colAdvisor);
        table.getColumns().add(colCap);
        table.getColumns().add(colMemCount);
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) fillForm(newSel);
        });
    }

    private void setupForm() {
        txtName = new TextField(); txtName.setPromptText("Kulüp Adı");
        txtDesc = new TextField(); txtDesc.setPromptText("Açıklama");
        txtAdvisor = new TextField(); txtAdvisor.setPromptText("Danışman Hoca");
        txtCapacity = new TextField(); txtCapacity.setPromptText("Kontenjan");
        dpFoundation = new DatePicker(); dpFoundation.setPromptText("Kuruluş Tarihi");
    }

    private VBox createFormBox() {
        VBox form = new VBox(15);
        form.setStyle("-fx-border-color: gray; -fx-padding: 15;");

        HBox row1 = new HBox(15, new Label("Ad:"), txtName, new Label("Tarih:"), dpFoundation, new Label("Kontenjan:"), txtCapacity);
        HBox row2 = new HBox(15, new Label("Danışman:"), txtAdvisor, new Label("Açıklama:"), txtDesc);
        txtDesc.setPrefWidth(300);

        Button btnAdd = new Button("Ekle");
        Button btnUpdate = new Button("Güncelle");
        Button btnDelete = new Button("Sil");
        Button btnClear = new Button("Temizle");

        HBox buttons = new HBox(15, btnAdd, btnUpdate, btnDelete, btnClear);

        btnAdd.setOnAction(e -> {
            if(validateInput()) {
                Club club = new Club(0, txtName.getText(), txtDesc.getText(), dpFoundation.getValue().toString(), txtAdvisor.getText(), Integer.parseInt(txtCapacity.getText()));
                if(dao.addClub(club)) {
                    showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Kulüp eklendi.");
                    loadData(); clearForm();
                }
            }
        });

        btnUpdate.setOnAction(e -> {
            Club selected = table.getSelectionModel().getSelectedItem();
            if(selected != null && validateInput()) {
                selected.setName(txtName.getText());
                selected.setDescription(txtDesc.getText());
                selected.setFoundationDate(dpFoundation.getValue().toString());
                selected.setAdvisorName(txtAdvisor.getText());
                selected.setCapacity(Integer.parseInt(txtCapacity.getText()));
                if(dao.updateClub(selected)) {
                    showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Kulüp güncellendi.");
                    loadData(); clearForm();
                }
            }
        });

        btnDelete.setOnAction(e -> {
            Club selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) {
                if(dao.deleteClub(selected.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Kulüp silindi.");
                    loadData(); clearForm();
                }
            }
        });

        btnClear.setOnAction(e -> clearForm());

        form.getChildren().addAll(new Label("Kulüp Bilgileri"), row1, row2, buttons);
        return form;
    }

    private boolean validateInput() {
        if(txtName.getText().isEmpty() || txtCapacity.getText().isEmpty() || dpFoundation.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Lütfen zorunlu alanları (Ad, Kontenjan, Tarih) doldurun.");
            return false;
        }
        try {
            Integer.parseInt(txtCapacity.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Kontenjan sayısal olmalıdır.");
            return false;
        }
        return true;
    }

    private void fillForm(Club club) {
        txtName.setText(club.getName());
        txtDesc.setText(club.getDescription());
        txtAdvisor.setText(club.getAdvisorName());
        txtCapacity.setText(String.valueOf(club.getCapacity()));
        if(club.getFoundationDate() != null) dpFoundation.setValue(LocalDate.parse(club.getFoundationDate()));
    }

    private void clearForm() {
        txtName.clear(); txtDesc.clear(); txtAdvisor.clear(); txtCapacity.clear(); dpFoundation.setValue(null);
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        data = FXCollections.observableArrayList(dao.getAllClubs());
        table.setItems(data);
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getView() { return view; }
}
