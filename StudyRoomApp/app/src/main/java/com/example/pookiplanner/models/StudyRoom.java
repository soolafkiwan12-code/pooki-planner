package com.example.pookiplanner.models;

public class StudyRoom {
    private String id;
    private String name;
    private String governorate;
    private int capacity;
    private String hours;
    private boolean booked;

    public StudyRoom(String id, String name, String governorate, int capacity, String hours) {
        this.id = id;
        this.name = name;
        this.governorate = governorate;
        this.capacity = capacity;
        this.hours = hours;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getGovernorate() { return governorate; }
    public int getCapacity() { return capacity; }
    public String getHours() { return hours; }
    public boolean isBooked() { return booked; }
    public void setBooked(boolean booked) { this.booked = booked; }
}
