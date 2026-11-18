package com.example.pookiplanner.models;

public class Booking {
    private long id;
    private String roomId;
    private String roomName;
    private String date;
    private String time;
    private long userId;

    public Booking() {}

    public Booking(String roomId, String roomName, String date, String time, long userId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.date = date;
        this.time = time;
        this.userId = userId;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getRoomId() { return roomId; }
    public void setRoomId(String roomId) { this.roomId = roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
}
