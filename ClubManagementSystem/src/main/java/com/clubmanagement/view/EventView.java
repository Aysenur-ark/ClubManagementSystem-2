package com.clubmanagement.view;

import com.clubmanagement.dao.ClubDAO;
import com.clubmanagement.dao.EventDAO;
import com.clubmanagement.model.Club;
import com.clubmanagement.model.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

public class EventView {
    private VBox view;
    private TableView<Event> table;
    private EventDAO dao;
    private ClubDAO clubDAO;
    private ObservableList<Event> data;
    
    private TextField txtName, txtLocation, txtDesc, txtAttendeeCount;
    private DatePicker dpDate;
    private ComboBox<Club> cbClub;

    public EventView() {
        dao = new EventDAO();
        clubDAO = new ClubDAO();
        view = new VBox(15);
        view.setPadding(new Insets(15));
        
        Label title = new Label("Etkinlik Yönetimi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        setupTable();
        setupForm();

        view.getChildren().addAll(title, table, createFormBox());
        loadData();
    }

    private void setupTable() {
        table = new TableView<>();

        TableColumn<Event, String> colName = new TableColumn<>("Etkinlik Adı");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Event, String> colDate = new TableColumn<>("Tarih");
        colDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));

        TableColumn<Event, String> colLocation = new TableColumn<>("Yer");
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Event, String> colDesc = new TableColumn<>("Açıklama");
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Event, Integer> colCount = new TableColumn<>("Katılımcı Sayısı");
        colCount.setCellValueFactory(new PropertyValueFactory<>("attendeeCount"));

        TableColumn<Event, String> colClub = new TableColumn<>("Düzenleyen Kulüp");
        colClub.setCellValueFactory(new PropertyValueFactory<>("clubName"));

        table.getColumns().add(colName);
        table.getColumns().add(colDate);
        table.getColumns().add(colLocation);
        table.getColumns().add(colDesc);
        table.getColumns().add(colCount);
        table.getColumns().add(colClub);
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) fillForm(newSel);
        });
    }

    private void setupForm() {
        txtName = new TextField(); txtName.setPromptText("Etkinlik Adı");
        txtLocation = new TextField(); txtLocation.setPromptText("Yer");
        txtDesc = new TextField(); txtDesc.setPromptText("Açıklama");
        txtAttendeeCount = new TextField(); txtAttendeeCount.setPromptText("Katılımcı Sayısı");
        dpDate = new DatePicker(); dpDate.setPromptText("Etkinlik Tarihi");
        cbClub = new ComboBox<>(FXCollections.observableArrayList(clubDAO.getAllClubs()));
        cbClub.setPromptText("Kulüp Seçin");
    }

    private VBox createFormBox() {
        VBox form = new VBox(15);
        form.setStyle("-fx-border-color: gray; -fx-padding: 15;");

        HBox row1 = new HBox(15, new Label("Etkinlik Adı:"), txtName, new Label("Tarih:"), dpDate, new Label("Yer:"), txtLocation, new Label("Katılımcı Sayısı:"), txtAttendeeCount);
        HBox row2 = new HBox(15, new Label("Kulüp:"), cbClub, new Label("Açıklama:"), txtDesc);
        txtDesc.setPrefWidth(300);

        Button btnAdd = new Button("Ekle");
        Button btnUpdate = new Button("Güncelle");
        Button btnDelete = new Button("Sil");
        Button btnClear = new Button("Temizle");

        HBox buttons = new HBox(15, btnAdd, btnUpdate, btnDelete, btnClear);

        btnAdd.setOnAction(e -> {
            if(validateInput()) {
                Event event = new Event(0, txtName.getText(), dpDate.getValue().toString(), txtLocation.getText(), 
                        txtDesc.getText(), Integer.parseInt(txtAttendeeCount.getText()), cbClub.getValue().getId());
                if(dao.addEvent(event)) {
                    showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Etkinlik eklendi.");
                    loadData(); clearForm();
                }
            }
        });

        btnUpdate.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if(selected != null && validateInput()) {
                selected.setName(txtName.getText());
                selected.setEventDate(dpDate.getValue().toString());
                selected.setLocation(txtLocation.getText());
                selected.setDescription(txtDesc.getText());
                selected.setAttendeeCount(Integer.parseInt(txtAttendeeCount.getText()));
                selected.setClubId(cbClub.getValue().getId());
                
                if(dao.updateEvent(selected)) {
                    showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Etkinlik güncellendi.");
                    loadData(); clearForm();
                }
            }
        });

        btnDelete.setOnAction(e -> {
            Event selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) {
                if(dao.deleteEvent(selected.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Etkinlik silindi.");
                    loadData(); clearForm();
                }
            }
        });

        btnClear.setOnAction(e -> clearForm());

        form.getChildren().addAll(new Label("Etkinlik Bilgileri"), row1, row2, buttons);
        return form;
    }

    private boolean validateInput() {
        if(txtName.getText().isEmpty() || dpDate.getValue() == null || cbClub.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Etkinlik Adı, Tarih ve Kulüp alanları zorunludur.");
            return false;
        }
        try {
            if(!txtAttendeeCount.getText().isEmpty()) Integer.parseInt(txtAttendeeCount.getText());
            else txtAttendeeCount.setText("0");
        } catch(NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Katılımcı Sayısı sayısal olmalıdır.");
            return false;
        }
        return true;
    }

    private void fillForm(Event event) {
        txtName.setText(event.getName());
        txtLocation.setText(event.getLocation());
        txtDesc.setText(event.getDescription());
        txtAttendeeCount.setText(String.valueOf(event.getAttendeeCount()));
        if(event.getEventDate() != null) dpDate.setValue(LocalDate.parse(event.getEventDate()));
        
        for(Club c : cbClub.getItems()) {
            if(c.getId() == event.getClubId()) {
                cbClub.setValue(c);
                break;
            }
        }
    }

    private void clearForm() {
        txtName.clear(); txtLocation.clear(); txtDesc.clear(); txtAttendeeCount.clear(); dpDate.setValue(null);
        cbClub.setValue(null);
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        data = FXCollections.observableArrayList(dao.getAllEvents());
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
