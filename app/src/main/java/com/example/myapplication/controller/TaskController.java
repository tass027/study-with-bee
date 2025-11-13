package com.example.myapplication.controller;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.myapplication.model.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class TaskController {

    private static final String PREFS_NAME = "TaskPrefs";
    private static final String TASKS_KEY = "tasks_list";
    private static final String REMINDERS_KEY = "reminders_list";

    private static ArrayList<Task> tasksList = new ArrayList<>();
    private static ArrayList<String> remindersList = new ArrayList<>();
    private static final Gson gson = new Gson();

    // ========== TASK METHODS ==========

    public static void addTask(String title, String description, String priority, String dueDate, Context context) {
        loadTasks(context);
        Task task = new Task(title, description, priority, dueDate);
        tasksList.add(task);
        saveTasks(context);
    }

    public static void addTask(String title, Context context) {
        addTask(title, "", "Low", "No due date", context);
    }

    public static void updateTask(int position, String title, String description, String priority, String dueDate, Context context) {
        loadTasks(context);
        if (position >= 0 && position < tasksList.size()) {
            Task task = tasksList.get(position);
            task.setTitle(title);
            task.setDescription(description);
            task.setPriority(priority);
            task.setDueDate(dueDate);
            saveTasks(context);
        }
    }

    public static void removeTask(int position, Context context) {
        loadTasks(context);
        if (position >= 0 && position < tasksList.size()) {
            tasksList.remove(position);
            saveTasks(context);
        }
    }

    public static Task getTask(int position, Context context) {
        loadTasks(context);
        if (position >= 0 && position < tasksList.size()) {
            return tasksList.get(position);
        }
        return null;
    }

    public static ArrayList<Task> getTasks(Context context) {
        loadTasks(context);
        return new ArrayList<>(tasksList);
    }

    public static void toggleTaskCompletion(int position, Context context) {
        loadTasks(context);
        if (position >= 0 && position < tasksList.size()) {
            Task task = tasksList.get(position);
            task.setCompleted(!task.isCompleted());
            saveTasks(context);
        }
    }

    public static void loadTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(TASKS_KEY, null);

        if (json != null) {
            try {
                Type type = new TypeToken<ArrayList<Task>>(){}.getType();
                tasksList = gson.fromJson(json, type);
            } catch (Exception e) {
                // JSON corrupted or old format, reset list
                e.printStackTrace();
                tasksList = new ArrayList<>();
                saveTasks(context);
            }
        } else {
            tasksList = new ArrayList<>();
        }
    }


    private static void saveTasks(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(tasksList);
        prefs.edit().putString(TASKS_KEY, json).apply();
    }

    public static void clearTasks(Context context) {
        tasksList.clear();
        saveTasks(context);
    }

    // ========== REMINDER METHODS ==========

    public static void addReminder(String reminder, Context context) {
        loadReminders(context);
        remindersList.add(reminder);
        saveReminders(context);
    }

    public static void removeReminder(int position, Context context) {
        loadReminders(context);
        if (position >= 0 && position < remindersList.size()) {
            remindersList.remove(position);
            saveReminders(context);
        }
    }

    public static ArrayList<String> getReminders(Context context) {
        loadReminders(context);
        return new ArrayList<>(remindersList);
    }

    private static void loadReminders(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(REMINDERS_KEY, null);

        if (json != null) {
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            remindersList = gson.fromJson(json, type);
        } else {
            remindersList = new ArrayList<>();
        }
    }

    private static void saveReminders(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(remindersList);
        prefs.edit().putString(REMINDERS_KEY, json).apply();
    }

    public static void clearAllReminders(Context context) {
        remindersList.clear();
        saveReminders(context);
    }
}
