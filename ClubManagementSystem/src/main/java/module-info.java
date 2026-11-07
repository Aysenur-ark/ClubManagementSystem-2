module com.clubmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // SQLite JDBC için gerekli

    // TableView'in model sınıflarındaki (get/set) metodlarına erişebilmesi için:
    opens com.clubmanagement.model to javafx.base;
    
    // Uygulama sınıfının başlatılabilmesi için ana paketi dışa açıyoruz:
    exports com.clubmanagement;
    exports com.clubmanagement.model;
    exports com.clubmanagement.view;
    exports com.clubmanagement.dao;
}
