package com.clubmanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:club_management.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createClubTable = "CREATE TABLE IF NOT EXISTS clubs ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "description TEXT,"
                + "foundationDate TEXT,"
                + "advisorName TEXT,"
                + "capacity INTEGER NOT NULL"
                + ");";

        String createMemberTable = "CREATE TABLE IF NOT EXISTS members ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "studentId TEXT UNIQUE NOT NULL,"
                + "email TEXT,"
                + "registrationDate TEXT,"
                + "role TEXT,"
                + "status TEXT,"
                + "clubId INTEGER,"
                + "FOREIGN KEY (clubId) REFERENCES clubs(id)"
                + ");";

        String createEventTable = "CREATE TABLE IF NOT EXISTS events ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "eventDate TEXT,"
                + "location TEXT,"
                + "description TEXT,"
                + "attendeeCount INTEGER,"
                + "clubId INTEGER,"
                + "FOREIGN KEY (clubId) REFERENCES clubs(id)"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Veritabanı tablolarını oluştur
            stmt.execute(createClubTable);
            stmt.execute(createMemberTable);
            stmt.execute(createEventTable);
            
            // Eğer kulüp tablosu boşsa 3 tane varsayılan kulüp ekle (Öğrenci testi için)
            try (java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM clubs")) {
                if (rs.next() && rs.getInt("total") == 0) {
                    stmt.execute("INSERT INTO clubs (name, description, foundationDate, advisorName, capacity) VALUES ('Yazılım Kulübü', 'Kodlama ve Teknoloji', '2023-01-01', 'Ahmet Yılmaz', 50)");
                    stmt.execute("INSERT INTO clubs (name, description, foundationDate, advisorName, capacity) VALUES ('Müzik Kulübü', 'Enstrüman ve Şan', '2022-05-15', 'Ayşe Demir', 30)");
                    stmt.execute("INSERT INTO clubs (name, description, foundationDate, advisorName, capacity) VALUES ('Doğa Kulübü', 'Kamp ve Yürüyüş', '2021-09-10', 'Mehmet Kaya', 40)");
                    System.out.println("3 Adet varsayılan test kulübü eklendi.");
                }
            }
            
            System.out.println("Veritabanı başarıyla başlatıldı ve tablolar kontrol edildi.");
            
        } catch (SQLException e) {
            System.err.println("Veritabanı başlatılırken hata oluştu: " + e.getMessage());
        }
    }
}
