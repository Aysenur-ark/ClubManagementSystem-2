package com.clubmanagement.model;

public class Club {
    private int id;
    private String name;
    private String description;
    private String foundationDate;
    private String advisorName;
    private int capacity;
    
    // Virtual property for displaying member count
    private int currentMemberCount;

    public Club() {}

    public Club(int id, String name, String description, String foundationDate, String advisorName, int capacity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.foundationDate = foundationDate;
        this.advisorName = advisorName;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFoundationDate() { return foundationDate; }
    public void setFoundationDate(String foundationDate) { this.foundationDate = foundationDate; }
    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getCurrentMemberCount() { return currentMemberCount; }
    public void setCurrentMemberCount(int currentMemberCount) { this.currentMemberCount = currentMemberCount; }

    @Override
    public String toString() {
        return name; // ComboBox gösterimi için
    }
}
