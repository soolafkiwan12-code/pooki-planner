package com.example.pookiplanner.models;

public class AaAACoffeeHouse {
    private String id;
    private String name;
    private String governorate;
    private String area;
    private String hours;
    private boolean hasWifi;
    private int rating;

    public CoffeeHouse(String id, String name, String governorate, String area,
                       String hours, boolean hasWifi, int rating) {
        this.id = id;
        this.name = name;
        this.governorate = governorate;
        this.area = area;
        this.hours = hours;
        this.hasWifi = hasWifi;
        this.rating = rating;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getGovernorate() { return governorate; }
    public String getArea() { return area; }
    public String getHours() { return hours; }
    public boolean isHasWifi() { return hasWifi; }
    public int getRating() { return rating; }
}
