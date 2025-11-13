package com.example.myapplication;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.controller.TaskController;
import com.example.myapplication.model.Task;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AchievementsActivity extends AppCompatActivity {

    // Statistics TextViews
    private TextView tvTotalTasks, tvCompletedTasks, tvStudyTime, tvStreak;
    private TextView tvWeeklyGoal, tvDailyGoal, tvCompletedSessions;
    private TextView tvStudyProgressLabel, tvTaskProgressLabel;
    private ProgressBar progressBarTasks, progressBarStudy;
    private MaterialButton btnSetGoals, btnViewHistory;

    // Badge Cards
    private CardView cardBeginner, cardIntermediate, cardExpert, cardMaster;

    // Constants for badge requirements
    private static final int BEGINNER_TASKS = 5;
    private static final int BEGINNER_TIME = 60;
    private static final int INTERMEDIATE_TASKS = 20;
    private static final int INTERMEDIATE_TIME = 300;
    private static final int EXPERT_TASKS = 50;
    private static final int EXPERT_TIME = 600;
    private static final int MASTER_TASKS = 100;
    private static final int MASTER_TIME = 1200;

    private SharedPreferences prefs;
    private int currentLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        prefs = getSharedPreferences("study_prefs", MODE_PRIVATE);
        initializeViews();
        loadStatistics();
        setupButtons();
    }

    private void initializeViews() {
        tvTotalTasks = findViewById(R.id.tvTotalTasks);
        tvCompletedTasks = findViewById(R.id.tvCompletedTasks);
        tvStudyTime = findViewById(R.id.tvStudyTime);
        tvStreak = findViewById(R.id.tvStreak);
        tvWeeklyGoal = findViewById(R.id.tvWeeklyGoal);
        tvDailyGoal = findViewById(R.id.tvDailyGoal);
        tvCompletedSessions = findViewById(R.id.tvCompletedSessions);
        tvStudyProgressLabel = findViewById(R.id.tvStudyProgressLabel);
        tvTaskProgressLabel = findViewById(R.id.tvTaskProgressLabel);

        progressBarTasks = findViewById(R.id.progressBarTasks);
        progressBarStudy = findViewById(R.id.progressBarStudy);

        btnSetGoals = findViewById(R.id.btnSetGoals);
        btnViewHistory = findViewById(R.id.btnViewHistory);

        cardBeginner = findViewById(R.id.cardBeginner);
        cardIntermediate = findViewById(R.id.cardIntermediate);
        cardExpert = findViewById(R.id.cardExpert);
        cardMaster = findViewById(R.id.cardMaster);
    }

    private void setupButtons() {
        btnSetGoals.setOnClickListener(v -> showGoalsDialog());
        btnViewHistory.setOnClickListener(v -> showDetailedHistory());
    }

    private void loadStatistics() {
        // Load data from ToDoList
        ArrayList<Task> allTasks = TaskController.getTasks(this);
        int totalTasks = allTasks != null ? allTasks.size() : 0;
        int completedTasks = 0;

        if (allTasks != null) {
            for (Task task : allTasks) {
                if (task.isCompleted()) {
                    completedTasks++;
                }
            }
        }

        // Load data from Timer
        int totalSessions = prefs.getInt("total_sessions", 0);
        int totalStudyTime = prefs.getInt("total_study_time", 0);

        // Calculate streak
        int streak = calculateStreak();

        // Load goals
        int weeklyGoal = prefs.getInt("weekly_goal", 7);
        int dailyGoal = prefs.getInt("daily_goal", 60);

        // Update UI
        updateStatistics(totalTasks, completedTasks, totalStudyTime, streak, totalSessions);
        updateGoalsDisplay(weeklyGoal, dailyGoal, totalSessions);
        updateProgressBars(totalTasks, completedTasks, totalStudyTime);
        updateBadgesAndLevel(completedTasks, totalStudyTime);
        displayPersonalizedAdvice(completedTasks, totalStudyTime, streak);
    }

    private void updateStatistics(int totalTasks, int completedTasks, int studyTime, int streak, int sessions) {
        tvTotalTasks.setText(String.valueOf(totalTasks));
        tvCompletedTasks.setText(String.valueOf(completedTasks));
        tvStudyTime.setText(studyTime + " min");
        tvStreak.setText(streak + " days");
        tvCompletedSessions.setText(sessions + " sessions");
    }

    private void updateGoalsDisplay(int weeklyGoal, int dailyGoal, int completedSessions) {
        int daysThisWeek = prefs.getInt("days_studied_this_week", 0);
        tvWeeklyGoal.setText("Weekly: " + daysThisWeek + "/" + weeklyGoal + " days");
        tvDailyGoal.setText("Daily: " + dailyGoal + " min");
    }

    private void updateProgressBars(int totalTasks, int completedTasks, int studyTime) {
        // Task completion progress
        int taskProgress = totalTasks == 0 ? 0 : (completedTasks * 100 / totalTasks);
        progressBarTasks.setProgress(taskProgress);
        tvTaskProgressLabel.setText("Task Completion Progress (" + taskProgress + "%)");

        // Study time progress
        int nextMilestone = getNextMilestone(studyTime);
        int studyProgress = nextMilestone == 0 ? 100 : Math.min((studyTime * 100 / nextMilestone), 100);
        progressBarStudy.setProgress(studyProgress);
        tvStudyProgressLabel.setText("Study Time Progress (Goal: " + nextMilestone + " min - " + studyProgress + "%)");
    }

    private int getNextMilestone(int currentTime) {
        if (currentTime < BEGINNER_TIME) return BEGINNER_TIME;
        if (currentTime < INTERMEDIATE_TIME) return INTERMEDIATE_TIME;
        if (currentTime < EXPERT_TIME) return EXPERT_TIME;
        if (currentTime < MASTER_TIME) return MASTER_TIME;
        return MASTER_TIME + 300;
    }

    private void updateBadgesAndLevel(int completedTasks, int studyTime) {
        boolean beginnerUnlocked = completedTasks >= BEGINNER_TASKS || studyTime >= BEGINNER_TIME;
        updateBadgeCard(cardBeginner, beginnerUnlocked);
        if (beginnerUnlocked) currentLevel = Math.max(currentLevel, 0);

        boolean intermediateUnlocked = completedTasks >= INTERMEDIATE_TASKS || studyTime >= INTERMEDIATE_TIME;
        updateBadgeCard(cardIntermediate, intermediateUnlocked);
        if (intermediateUnlocked) currentLevel = Math.max(currentLevel, 1);

        boolean expertUnlocked = completedTasks >= EXPERT_TASKS || studyTime >= EXPERT_TIME;
        updateBadgeCard(cardExpert, expertUnlocked);
        if (expertUnlocked) currentLevel = Math.max(currentLevel, 2);

        boolean masterUnlocked = completedTasks >= MASTER_TASKS || studyTime >= MASTER_TIME;
        updateBadgeCard(cardMaster, masterUnlocked);
        if (masterUnlocked) currentLevel = Math.max(currentLevel, 3);

        prefs.edit().putInt("current_level", currentLevel).apply();
    }

    private void updateBadgeCard(CardView card, boolean isUnlocked) {
        if (isUnlocked) {
            card.setAlpha(1.0f);
            card.setCardElevation(8f);
            card.setCardBackgroundColor(Color.parseColor("#FFD700"));
        } else {
            card.setAlpha(0.4f);
            card.setCardElevation(2f);
            card.setCardBackgroundColor(Color.parseColor("#CCCCCC"));
        }
    }

    private void displayPersonalizedAdvice(int completedTasks, int studyTime, int streak) {
        String advice = getAdviceForLevel(currentLevel, completedTasks, studyTime, streak);
        String levelName = getLevelName(currentLevel);

        new AlertDialog.Builder(this)
                .setTitle("💡 " + levelName + " - Personal Advice")
                .setMessage(advice)
                .setPositiveButton("Got it!", null)
                .show();
    }

    private String getAdviceForLevel(int level, int tasks, int studyTime, int streak) {
        switch (level) {
            case 0:
                if (tasks < 3) {
                    return "🐝 Welcome, Beginner Bee!\n\n" +
                            "Start by adding 3 tasks today. Small steps lead to big achievements!\n\n" +
                            "Tip: Break large tasks into smaller, manageable pieces.";
                } else if (studyTime < 30) {
                    return "Great start! 🌟\n\n" +
                            "Try to complete a 25-minute study session today using the timer.\n\n" +
                            "Tip: Use the Pomodoro technique - 25 min work, 5 min break.";
                } else {
                    return "You're making excellent progress! 🎉\n\n" +
                            "Keep up the momentum. Next goal: " + INTERMEDIATE_TASKS + " tasks!\n\n" +
                            "Tip: Consistency is more important than intensity.";
                }

            case 1:
                if (streak < 3) {
                    return "📚 Study Bee!\n\n" +
                            "Build consistency by studying 3 days in a row. Consistency is key to success!\n\n" +
                            "Tip: Set a daily reminder to study at the same time each day.";
                } else if (studyTime < 200) {
                    return "Good progress! 💪\n\n" +
                            "Aim for 45-minute study sessions. You're building great habits!\n\n" +
                            "Tip: Take short breaks between sessions to stay fresh.";
                } else {
                    return "Impressive work ethic! 🌟\n\n" +
                            "Challenge yourself: complete " + EXPERT_TASKS + " tasks for Expert status!\n\n" +
                            "Tip: Track your most productive hours and schedule important tasks then.";
                }

            case 2:
                if (streak < 7) {
                    return "🎓 Expert Bee!\n\n" +
                            "Build a 7-day streak for maximum productivity. You're so close to mastery!\n\n" +
                            "Tip: Even 15 minutes of study counts - never break the streak!";
                } else if (tasks < 40) {
                    return "Outstanding dedication! 🏆\n\n" +
                            "Focus on quality tasks. Each completed task brings you closer to Master!\n\n" +
                            "Tip: Review your completed tasks weekly to see your progress.";
                } else {
                    return "Exceptional performance! ⭐\n\n" +
                            "You're approaching Master level. Keep pushing your limits!\n\n" +
                            "Tip: Share your techniques with others - teaching reinforces learning.";
                }

            case 3:
                if (streak >= 14) {
                    return "👑 Master Bee - LEGENDARY STATUS!\n\n" +
                            "2+ weeks streak - You're a productivity legend!\n\n" +
                            "Tip: Consider mentoring others on their productivity journey!";
                } else if (studyTime >= 1500) {
                    return "👑 Master Bee - INCREDIBLE DEDICATION!\n\n" +
                            "25+ hours of focused study. You've mastered the art of productivity!\n\n" +
                            "Tip: Document your journey - it could inspire thousands!";
                } else {
                    return "👑 Master Bee - EXCELLENCE ACHIEVED!\n\n" +
                            "Master status achieved! Now maintain your excellence and inspire others.\n\n" +
                            "Tip: Set new, ambitious goals to keep growing!";
                }

            default:
                return "Keep learning and growing! 🌟\n\n" +
                        "Every task completed is a step forward!\n\n" +
                        "Tip: Start small, stay consistent, achieve greatness!";
        }
    }

    private int calculateStreak() {
        long lastStudyDate = prefs.getLong("last_study_date", 0);
        long today = Calendar.getInstance().getTimeInMillis();

        if (lastStudyDate == 0) return 0;

        long daysDifference = (today - lastStudyDate) / (1000 * 60 * 60 * 24);

        if (daysDifference <= 1) {
            return prefs.getInt("current_streak", 0);
        } else {
            prefs.edit().putInt("current_streak", 0).apply();
            return 0;
        }
    }

    private void showGoalsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🎯 Set Your Goals");

        String currentGoals = "Current Goals:\n\n" +
                "📅 Weekly Goal: Study for 7 days\n" +
                "⏱️ Daily Goal: 60 minutes of focused study\n\n" +
                "These goals help you stay on track and build consistency!";

        builder.setMessage(currentGoals);
        builder.setPositiveButton("Keep Goals", (dialog, which) -> {
            Toast.makeText(this, "✅ Goals maintained!", Toast.LENGTH_SHORT).show();
        });
        builder.setNeutralButton("Custom Goals", (dialog, which) -> {
            Toast.makeText(this, "Custom goals coming soon! 🚀", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDetailedHistory() {
        ArrayList<Task> allTasks = TaskController.getTasks(this);
        int totalTasks = allTasks != null ? allTasks.size() : 0;
        int completedTasks = 0;
        int pendingTasks = 0;
        int highPriorityTasks = 0;
        int mediumPriorityTasks = 0;
        int lowPriorityTasks = 0;

        if (allTasks != null) {
            for (Task task : allTasks) {
                if (task.isCompleted()) {
                    completedTasks++;
                } else {
                    pendingTasks++;
                }

                String priority = task.getPriority();
                if (priority.equals("High")) highPriorityTasks++;
                else if (priority.equals("Medium")) mediumPriorityTasks++;
                else lowPriorityTasks++;
            }
        }

        int totalSessions = prefs.getInt("total_sessions", 0);
        int totalStudyTime = prefs.getInt("total_study_time", 0);
        int avgSessionTime = totalSessions > 0 ? totalStudyTime / totalSessions : 0;
        int streak = calculateStreak();
        int maxStreak = prefs.getInt("max_streak", 0);

        String history = "📊 DETAILED STATISTICS 📊\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "📝 TASK ANALYSIS:\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "   • Total Tasks: " + totalTasks + "\n" +
                "   • ✅ Completed: " + completedTasks + "\n" +
                "   • ⏳ Pending: " + pendingTasks + "\n" +
                "   • 📈 Completion Rate: " + (totalTasks > 0 ? (completedTasks * 100 / totalTasks) : 0) + "%\n\n" +
                "Priority Breakdown:\n" +
                "   • 🔴 High: " + highPriorityTasks + "\n" +
                "   • 🟡 Medium: " + mediumPriorityTasks + "\n" +
                "   • 🟢 Low: " + lowPriorityTasks + "\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "⏱️ STUDY TIME ANALYSIS:\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "   • Total Sessions: " + totalSessions + " 🎯\n" +
                "   • Total Time: " + totalStudyTime + " min\n" +
                "   • Equivalent: " + (totalStudyTime / 60) + "h " + (totalStudyTime % 60) + "m\n" +
                "   • Average/Session: " + avgSessionTime + " min\n" +
                "   • Longest Session: " + prefs.getInt("longest_session", 0) + " min\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "🔥 STREAK INFORMATION:\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "   • Current Streak: " + streak + " days 🔥\n" +
                "   • Best Streak: " + maxStreak + " days 🏆\n" +
                "   • Study Days: " + prefs.getInt("total_study_days", 0) + "\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "🏅 YOUR LEVEL:\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "   • Current: " + getLevelName(currentLevel) + "\n" +
                "   • " + getLevelProgress(completedTasks, totalStudyTime) + "\n\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                "💡 PERSONALIZED TIP:\n" +
                "━━━━━━━━━━━━━━━━━━━━━━\n" +
                getShortAdvice(currentLevel, completedTasks, totalStudyTime, streak);

        new AlertDialog.Builder(this)
                .setTitle("📈 Complete Progress Report")
                .setMessage(history)
                .setPositiveButton("Awesome!", null)
                .setNeutralButton("Share", (dialog, which) -> {
                    Toast.makeText(this, "Sharing feature coming soon! 🎉", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private String getShortAdvice(int level, int tasks, int studyTime, int streak) {
        switch (level) {
            case 0: return "Focus on building the habit. Start with small, achievable goals! 🐝";
            case 1: return "Great progress! Aim for consistency over intensity. 📚";
            case 2: return "You're doing excellent! Push for longer streaks. 🎓";
            case 3: return "Master level! Keep inspiring others with your dedication! 👑";
            default: return "Every journey starts with a single step. You got this! 🌟";
        }
    }

    private String getLevelName(int level) {
        switch (level) {
            case 0: return "🐝 Beginner Bee";
            case 1: return "📚 Study Bee";
            case 2: return "🎓 Expert Bee";
            case 3: return "👑 Master Bee";
            default: return "🌟 Getting Started";
        }
    }

    private String getLevelProgress(int tasks, int studyTime) {
        if (currentLevel < 3) {
            int nextLevelTasks = getNextLevelRequirement(currentLevel);
            int nextLevelTime = getNextLevelTimeRequirement(currentLevel);
            int tasksRemaining = Math.max(0, nextLevelTasks - tasks);
            int timeRemaining = Math.max(0, nextLevelTime - studyTime);
            return "Next Level Progress:\n" +
                    "   • " + tasksRemaining + " more tasks OR\n" +
                    "   • " + timeRemaining + " more minutes needed";
        } else {
            return "🎉 Maximum level achieved!\n" +
                    "   • Keep up the excellent work!";
        }
    }

    private int getNextLevelRequirement(int currentLevel) {
        switch (currentLevel) {
            case 0: return INTERMEDIATE_TASKS;
            case 1: return EXPERT_TASKS;
            case 2: return MASTER_TASKS;
            default: return MASTER_TASKS;
        }
    }

    private int getNextLevelTimeRequirement(int currentLevel) {
        switch (currentLevel) {
            case 0: return INTERMEDIATE_TIME;
            case 1: return EXPERT_TIME;
            case 2: return MASTER_TIME;
            default: return MASTER_TIME;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics();
    }
}