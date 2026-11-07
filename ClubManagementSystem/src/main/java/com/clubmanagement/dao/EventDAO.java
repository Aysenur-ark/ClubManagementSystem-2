package com.clubmanagement.dao;

import com.clubmanagement.model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.*, c.name as clubName FROM events e LEFT JOIN clubs c ON e.clubId = c.id";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("eventDate"),
                        rs.getString("location"),
                        rs.getString("description"),
                        rs.getInt("attendeeCount"),
                        rs.getInt("clubId")
                );
                event.setClubName(rs.getString("clubName"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public boolean addEvent(Event event) {
        String query = "INSERT INTO events (name, eventDate, location, description, attendeeCount, clubId) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getEventDate());
            pstmt.setString(3, event.getLocation());
            pstmt.setString(4, event.getDescription());
            pstmt.setInt(5, event.getAttendeeCount());
            pstmt.setInt(6, event.getClubId());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEvent(Event event) {
        String query = "UPDATE events SET name=?, eventDate=?, location=?, description=?, attendeeCount=?, clubId=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, event.getName());
            pstmt.setString(2, event.getEventDate());
            pstmt.setString(3, event.getLocation());
            pstmt.setString(4, event.getDescription());
            pstmt.setInt(5, event.getAttendeeCount());
            pstmt.setInt(6, event.getClubId());
            pstmt.setInt(7, event.getId());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEvent(int id) {
        String query = "DELETE FROM events WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
