package com.clubmanagement.model;

public class Event {
    private int id;
    private String name;
    private String eventDate;
    private String location;
    private String description;
    private int attendeeCount;
    private int clubId;
    
    // Virtual property
    private String clubName;

    public Event() {}

    public Event(int id, String name, String eventDate, String location, String description, int attendeeCount, int clubId) {
        this.id = id;
        this.name = name;
        this.eventDate = eventDate;
        this.location = location;
        this.description = description;
        this.attendeeCount = attendeeCount;
        this.clubId = clubId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getAttendeeCount() { return attendeeCount; }
    public void setAttendeeCount(int attendeeCount) { this.attendeeCount = attendeeCount; }
    public int getClubId() { return clubId; }
    public void setClubId(int clubId) { this.clubId = clubId; }
    public String getClubName() { return clubName; }
    public void setClubName(String clubName) { this.clubName = clubName; }
}
