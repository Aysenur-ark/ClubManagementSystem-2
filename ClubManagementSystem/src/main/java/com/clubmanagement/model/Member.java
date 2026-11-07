package com.clubmanagement.model;

public class Member {
    private int id;
    private String name;
    private String studentId;
    private String email;
    private String registrationDate;
    private String role; // Başkan, Başkan Yardımcısı, Sekreter, Üye
    private String status; // Aktif, Pasif
    private int clubId;
    
    // Virtual property for UI
    private String clubName;

    public Member() {}

    public Member(int id, String name, String studentId, String email, String registrationDate, String role, String status, int clubId) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
        this.email = email;
        this.registrationDate = registrationDate;
        this.role = role;
        this.status = status;
        this.clubId = clubId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getClubId() { return clubId; }
    public void setClubId(int clubId) { this.clubId = clubId; }
    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }
}
