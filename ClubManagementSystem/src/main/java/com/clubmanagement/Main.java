package com.clubmanagement;

import com.clubmanagement.dao.DatabaseManager;
import com.clubmanagement.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Veritabanını başlat
        DatabaseManager.initializeDatabase();

        // Giriş ekranını göster
        LoginView loginView = new LoginView(primaryStage);
        Scene scene = loginView.getScene();

        primaryStage.setTitle("Kulüp Yönetim Sistemi");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
