package com.example.myapplication.model;

public class Task {
    private String title;
    private String description;
    private String priority;
    private String dueDate;
    private boolean isCompleted;

    public Task(String title, String description, String priority, String dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getDueDate() { return dueDate; }
    public boolean isCompleted() { return isCompleted; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    // Get priority color
    public int getPriorityColor() {
        switch (priority.toLowerCase()) {
            case "high": return 0xFFE91E63; // Pink
            case "medium": return 0xFFFFB347; // Orange
            case "low":
            default: return 0xFF4CAF50; // Green
        }
    }

    // Get priority emoji
    public String getPriorityEmoji() {
        switch (priority.toLowerCase()) {
            case "high": return "🔴";
            case "medium": return "🟠";
            case "low":
            default: return "🟢";
        }
    }
}
