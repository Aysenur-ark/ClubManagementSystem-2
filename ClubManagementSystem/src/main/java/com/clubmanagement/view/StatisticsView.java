package com.clubmanagement.view;

import com.clubmanagement.dao.ClubDAO;
import com.clubmanagement.model.Club;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class StatisticsView {
    private VBox view;
    private ClubDAO clubDAO;

    public StatisticsView() {
        clubDAO = new ClubDAO();
        
        view = new VBox(20);
        view.setPadding(new Insets(15));
        
        Label title = new Label("İstatistik ve Grafikler");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox chartsBox = new HBox(30);
        chartsBox.getChildren().addAll(createPieChart(), createBarChart());

        view.getChildren().addAll(title, chartsBox);
    }

    private PieChart createPieChart() {
        List<Club> clubs = clubDAO.getAllClubs();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        for(Club c : clubs) {
            int memberCount = clubDAO.getMemberCountByClubId(c.getId());
            if(memberCount > 0) {
                pieChartData.add(new PieChart.Data(c.getName() + " (" + memberCount + ")", memberCount));
            }
        }
        
        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Kulüp Tercih Oranları");
        chart.setPrefSize(400, 400);
        return chart;
    }

    private BarChart<String, Number> createBarChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Kulüpler");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Üye Sayısı");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Kulüplere Göre Üye Dağılımı");
        barChart.setPrefSize(500, 400);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Üyeler");
        
        List<Club> clubs = clubDAO.getAllClubs();
        for(Club c : clubs) {
            int memberCount = clubDAO.getMemberCountByClubId(c.getId());
            series.getData().add(new XYChart.Data<>(c.getName(), memberCount));
        }

        barChart.getData().add(series);
        return barChart;
    }

    public VBox getView() { return view; }
}
