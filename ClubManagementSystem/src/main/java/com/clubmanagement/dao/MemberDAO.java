package com.clubmanagement.dao;

import com.clubmanagement.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        // JOIN kullanarak kulüp adını da getiriyoruz
        String query = "SELECT m.*, c.name as clubName FROM members m LEFT JOIN clubs c ON m.clubId = c.id";
        
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Member member = new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("studentId"),
                        rs.getString("email"),
                        rs.getString("registrationDate"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getInt("clubId")
                );
                member.setClubName(rs.getString("clubName"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public boolean isStudentAlreadyInClub(String studentId, int clubId) {
        String query = "SELECT COUNT(*) AS total FROM members WHERE studentId=? AND clubId=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, studentId);
            pstmt.setInt(2, clubId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addMember(Member member) throws Exception {
        if (isStudentAlreadyInClub(member.getStudentId(), member.getClubId())) {
            throw new Exception("Bu öğrenci zaten bu kulübe kayıtlı!");
        }

        String query = "INSERT INTO members (name, studentId, email, registrationDate, role, status, clubId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getStudentId());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getRegistrationDate());
            pstmt.setString(5, member.getRole());
            pstmt.setString(6, member.getStatus());
            pstmt.setInt(7, member.getClubId());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            if(e.getMessage().contains("UNIQUE constraint failed")) {
                throw new Exception("Bu Öğrenci Numarası sistemde başka birine ait.");
            }
            throw e;
        }
    }

    public boolean updateMember(Member member) throws Exception {
        String query = "UPDATE members SET name=?, studentId=?, email=?, registrationDate=?, role=?, status=?, clubId=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getStudentId());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getRegistrationDate());
            pstmt.setString(5, member.getRole());
            pstmt.setString(6, member.getStatus());
            pstmt.setInt(7, member.getClubId());
            pstmt.setInt(8, member.getId());
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean deleteMember(int id) {
        String query = "DELETE FROM members WHERE id=?";
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
