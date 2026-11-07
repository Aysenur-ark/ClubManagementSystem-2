package com.clubmanagement.view;

import com.clubmanagement.dao.ClubDAO;
import com.clubmanagement.dao.EventDAO;
import com.clubmanagement.dao.MemberDAO;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class HomeView {
    private VBox view;
    private ClubDAO clubDAO;
    private MemberDAO memberDAO;
    private EventDAO eventDAO;

    public HomeView() {
        clubDAO = new ClubDAO();
        memberDAO = new MemberDAO();
        eventDAO = new EventDAO();
        
        view = new VBox(20);
        view.setPadding(new Insets(20));

        Label title = new Label("Sisteme Hoş Geldiniz");
        title.setFont(new Font("Arial", 20));

        Label subtitle = new Label("Kulüp Yönetim Sistemi Genel Durum Özeti");

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);

        int totalClubs = clubDAO.getAllClubs().size();
        int totalMembers = memberDAO.getAllMembers().size();
        long activeMembers = memberDAO.getAllMembers().stream().filter(m -> "Aktif".equals(m.getStatus())).count();
        int totalEvents = eventDAO.getAllEvents().size();

        grid.add(createStatCard("Toplam Kulüp", String.valueOf(totalClubs)), 0, 0);
        grid.add(createStatCard("Toplam Üye", String.valueOf(totalMembers)), 1, 0);
        grid.add(createStatCard("Aktif Üye", String.valueOf(activeMembers)), 0, 1);
        grid.add(createStatCard("Yaklaşan Etkinlikler", String.valueOf(totalEvents)), 1, 1);

        view.getChildren().addAll(title, subtitle, grid);
    }

    private VBox createStatCard(String title, String value) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 15; -fx-background-color: #f9f9f9;");
        card.setPrefWidth(200);

        Label lblTitle = new Label(title);
        Label lblValue = new Label(value);
        lblValue.setFont(new Font("Arial", 24));

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    public VBox getView() {
        return view;
    }
}
