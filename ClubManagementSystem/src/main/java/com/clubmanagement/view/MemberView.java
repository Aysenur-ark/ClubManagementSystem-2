package com.clubmanagement.view;

import com.clubmanagement.dao.ClubDAO;
import com.clubmanagement.dao.MemberDAO;
import com.clubmanagement.model.Club;
import com.clubmanagement.model.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MemberView {
    private VBox view;
    private TableView<Member> table;
    private MemberDAO dao;
    private ClubDAO clubDAO;
    private ObservableList<Member> data;
    
    private TextField txtName, txtStudentId, txtEmail, txtSearch;
    private DatePicker dpRegDate;
    private ComboBox<String> cbRole, cbStatus;
    private ComboBox<Club> cbClub;

    public MemberView() {
        dao = new MemberDAO();
        clubDAO = new ClubDAO();
        view = new VBox(15);
        view.setPadding(new Insets(15));
        
        Label title = new Label("Üye Yönetimi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        txtSearch = new TextField();
        txtSearch.setPromptText("Üye Adına Göre Ara...");
        txtSearch.textProperty().addListener((obs, oldV, newV) -> filterData(newV));

        setupTable();
        setupForm();

        view.getChildren().addAll(title, txtSearch, table, createFormBox());
        loadData();
    }

    private void setupTable() {
        table = new TableView<>();

        TableColumn<Member, String> colName = new TableColumn<>("Ad Soyad");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Member, String> colStdId = new TableColumn<>("Öğrenci No");
        colStdId.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        TableColumn<Member, String> colEmail = new TableColumn<>("E-posta");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Member, String> colRole = new TableColumn<>("Rol");
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        TableColumn<Member, String> colStatus = new TableColumn<>("Durum");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Member, String> colClub = new TableColumn<>("Kulüp");
        colClub.setCellValueFactory(new PropertyValueFactory<>("clubName"));

        table.getColumns().add(colName);
        table.getColumns().add(colStdId);
        table.getColumns().add(colEmail);
        table.getColumns().add(colRole);
        table.getColumns().add(colStatus);
        table.getColumns().add(colClub);
        
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) fillForm(newSel);
        });
    }

    private void setupForm() {
        txtName = new TextField(); txtName.setPromptText("Ad Soyad");
        txtStudentId = new TextField(); txtStudentId.setPromptText("Öğrenci No");
        txtEmail = new TextField(); txtEmail.setPromptText("E-posta");
        dpRegDate = new DatePicker(); dpRegDate.setPromptText("Kayıt Tarihi");
        
        cbRole = new ComboBox<>(FXCollections.observableArrayList("Başkan", "Başkan Yardımcısı", "Sekreter", "Üye"));
        cbRole.setPromptText("Rol Seçin");
        
        cbStatus = new ComboBox<>(FXCollections.observableArrayList("Aktif", "Pasif"));
        cbStatus.setPromptText("Durum Seçin");
        
        cbClub = new ComboBox<>(FXCollections.observableArrayList(clubDAO.getAllClubs()));
        cbClub.setPromptText("Kulüp Seçin");
    }

    private VBox createFormBox() {
        VBox form = new VBox(15);
        form.setStyle("-fx-border-color: gray; -fx-padding: 15;");

        HBox row1 = new HBox(15, new Label("Ad Soyad:"), txtName, new Label("Öğrenci No:"), txtStudentId, new Label("E-posta:"), txtEmail);
        HBox row2 = new HBox(15, new Label("Kayıt Tarihi:"), dpRegDate, new Label("Rol:"), cbRole, new Label("Durum:"), cbStatus, new Label("Kulüp:"), cbClub);

        Button btnAdd = new Button("Ekle");
        Button btnUpdate = new Button("Güncelle");
        Button btnDelete = new Button("Sil");
        Button btnClear = new Button("Temizle");

        HBox buttons = new HBox(15, btnAdd, btnUpdate, btnDelete, btnClear);

        btnAdd.setOnAction(e -> {
            if(validateInput()) {
                if(!checkCapacity()) return;
                try {
                    Member member = new Member(0, txtName.getText(), txtStudentId.getText(), txtEmail.getText(), 
                            dpRegDate.getValue() != null ? dpRegDate.getValue().toString() : LocalDate.now().toString(),
                            cbRole.getValue(), cbStatus.getValue(), cbClub.getValue().getId());
                    if(dao.addMember(member)) {
                        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Üye eklendi.");
                        loadData(); clearForm();
                    }
                } catch(Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Hata", ex.getMessage());
                }
            }
        });

        btnUpdate.setOnAction(e -> {
            Member selected = table.getSelectionModel().getSelectedItem();
            if(selected != null && validateInput()) {
                try {
                    selected.setName(txtName.getText());
                    selected.setStudentId(txtStudentId.getText());
                    selected.setEmail(txtEmail.getText());
                    selected.setRole(cbRole.getValue());
                    selected.setStatus(cbStatus.getValue());
                    selected.setClubId(cbClub.getValue().getId());
                    if(dpRegDate.getValue() != null) selected.setRegistrationDate(dpRegDate.getValue().toString());
                    
                    if(dao.updateMember(selected)) {
                        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Üye güncellendi.");
                        loadData(); clearForm();
                    }
                } catch(Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Hata", ex.getMessage());
                }
            }
        });

        btnDelete.setOnAction(e -> {
            Member selected = table.getSelectionModel().getSelectedItem();
            if(selected != null) {
                if(dao.deleteMember(selected.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Üye silindi.");
                    loadData(); clearForm();
                }
            }
        });

        btnClear.setOnAction(e -> clearForm());

        form.getChildren().addAll(new Label("Üye Bilgileri"), row1, row2, buttons);
        return form;
    }
    
    private boolean checkCapacity() {
        Club selectedClub = cbClub.getValue();
        if(selectedClub == null) return false;
        int currentCount = clubDAO.getMemberCountByClubId(selectedClub.getId());
        if(currentCount >= selectedClub.getCapacity()) {
            showAlert(Alert.AlertType.WARNING, "Kontenjan Dolu", "Seçilen kulübün kontenjanı dolmuştur. Yeni üye eklenemez.");
            return false;
        }
        return true;
    }

    private boolean validateInput() {
        if(txtName.getText().isEmpty() || txtStudentId.getText().isEmpty() || cbClub.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Ad, Öğrenci No ve Kulüp alanları zorunludur.");
            return false;
        }
        if(!txtEmail.getText().isEmpty() && !txtEmail.getText().contains("@")) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Geçersiz E-posta formatı.");
            return false;
        }
        return true;
    }

    private void fillForm(Member member) {
        txtName.setText(member.getName());
        txtStudentId.setText(member.getStudentId());
        txtEmail.setText(member.getEmail());
        if(member.getRegistrationDate() != null && !member.getRegistrationDate().isEmpty()) 
            dpRegDate.setValue(LocalDate.parse(member.getRegistrationDate()));
        cbRole.setValue(member.getRole());
        cbStatus.setValue(member.getStatus());
        
        for(Club c : cbClub.getItems()) {
            if(c.getId() == member.getClubId()) {
                cbClub.setValue(c);
                break;
            }
        }
    }

    private void clearForm() {
        txtName.clear(); txtStudentId.clear(); txtEmail.clear(); dpRegDate.setValue(null);
        cbRole.setValue(null); cbStatus.setValue(null); cbClub.setValue(null);
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        data = FXCollections.observableArrayList(dao.getAllMembers());
        table.setItems(data);
    }
    
    private void filterData(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            table.setItems(data);
            return;
        }
        List<Member> filtered = data.stream()
                .filter(m -> m.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        table.setItems(FXCollections.observableArrayList(filtered));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public VBox getView() { return view; }
}
