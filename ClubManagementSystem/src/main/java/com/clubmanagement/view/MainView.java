package com.clubmanagement.view;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainView {
    private Scene scene;
    private BorderPane root;

    public MainView(Stage stage) {
        root = new BorderPane();
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabHome = new Tab("Ana Sayfa", new HomeView().getView());
        Tab tabClubs = new Tab("Kulüp Yönetimi", new ClubView().getView());
        Tab tabMembers = new Tab("Üye Yönetimi", new MemberView().getView());
        Tab tabEvents = new Tab("Etkinlikler", new EventView().getView());
        Tab tabStats = new Tab("İstatistikler", new StatisticsView().getView());

        tabPane.getTabs().addAll(tabHome, tabClubs, tabMembers, tabEvents, tabStats);
        
        root.setCenter(tabPane);
        
        scene = new Scene(root, 1000, 700);
    }

    public Scene getScene() {
        return scene;
    }
}
