package com.clubmanagement.dao;

import com.clubmanagement.model.Club;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClubDAO {

    public List<Club> getAllClubs() {
        List<Club> clubs = new ArrayList<>();
        String query = "SELECT * FROM clubs";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Club club = new Club(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("foundationDate"),
                        rs.getString("advisorName"),
                        rs.getInt("capacity")
                );
                club.setCurrentMemberCount(getMemberCountByClubId(club.getId()));
                clubs.add(club);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clubs;
    }

    public boolean addClub(Club club) {
        String query = "INSERT INTO clubs (name, description, foundationDate, advisorName, capacity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setString(3, club.getFoundationDate());
            pstmt.setString(4, club.getAdvisorName());
            pstmt.setInt(5, club.getCapacity());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateClub(Club club) {
        String query = "UPDATE clubs SET name=?, description=?, foundationDate=?, advisorName=?, capacity=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, club.getName());
            pstmt.setString(2, club.getDescription());
            pstmt.setString(3, club.getFoundationDate());
            pstmt.setString(4, club.getAdvisorName());
            pstmt.setInt(5, club.getCapacity());
            pstmt.setInt(6, club.getId());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteClub(int id) {
        String query = "DELETE FROM clubs WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getMemberCountByClubId(int clubId) {
        String query = "SELECT COUNT(*) AS total FROM members WHERE clubId=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
