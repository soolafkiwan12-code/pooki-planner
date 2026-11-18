package com.example.pookiplanner.models;

public class StudyTask {
    private long id;
    private String title;
    private String description;
    private String dueDate;
    private boolean completed;
    private long userId;

    public StudyTask() {}

    public StudyTask(String title, String description, String dueDate, long userId) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.userId = userId;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }
}
