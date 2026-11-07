package com.clubmanagement.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginView {
    private Scene scene;

    public LoginView(Stage stage) {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));

        Label title = new Label("Kulüp Yönetim Sistemi");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        Label lblUser = new Label("Kullanıcı Adı:");
        TextField txtUsername = new TextField();

        Label lblPass = new Label("Şifre:");
        PasswordField txtPassword = new PasswordField();

        grid.add(lblUser, 0, 0);
        grid.add(txtUsername, 1, 0);
        grid.add(lblPass, 0, 1);
        grid.add(txtPassword, 1, 1);

        Button btnLogin = new Button("Giriş Yap");
        btnLogin.setPrefWidth(150);

        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red;");

        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();

            if ("admin".equals(username) && "1234".equals(password)) {
                MainView mainView = new MainView(stage);
                stage.setScene(mainView.getScene());
                stage.setMaximized(true);
            } else {
                lblError.setText("Hatalı kullanıcı adı veya şifre!");
            }
        });

        root.getChildren().addAll(title, grid, btnLogin, lblError);
        scene = new Scene(root, 600, 400);
    }

    public Scene getScene() {
        return scene;
    }
}
