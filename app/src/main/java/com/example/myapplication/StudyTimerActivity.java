package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;

public class StudyTimerActivity extends AppCompatActivity {

    private TextView tvTimer, tvTimerStatus, tvSessionCount, tvTotalTime;
    private MaterialButton btnStart, btnPause, btnReset;
    private MaterialButton btn25Min, btn45Min, btn60Min;
    private MaterialButton btnSaveProgress, btnViewAchievements;

    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis = 25 * 60 * 1000;
    private int currentDuration = 25;

    private int sessionCount = 0;
    private int totalMinutes = 0;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_timer);

        prefs = getSharedPreferences("study_prefs", MODE_PRIVATE);

        tvTimer = findViewById(R.id.tv_timer);
        tvTimerStatus = findViewById(R.id.tv_timer_status);
        tvSessionCount = findViewById(R.id.tv_session_count);
        tvTotalTime = findViewById(R.id.tv_total_time);

        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnReset = findViewById(R.id.btn_reset);
        btn25Min = findViewById(R.id.btn_25min);
        btn45Min = findViewById(R.id.btn_45min);
        btn60Min = findViewById(R.id.btn_60min);
        btnSaveProgress = findViewById(R.id.btn_save_progress);
        btnViewAchievements = findViewById(R.id.btn_view_achievements);

        loadSavedData();
        updateTimerText();

        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseTimer());
        btnReset.setOnClickListener(v -> resetTimer());
        btn25Min.setOnClickListener(v -> setDuration(25));
        btn45Min.setOnClickListener(v -> setDuration(45));
        btn60Min.setOnClickListener(v -> setDuration(60));
        btnSaveProgress.setOnClickListener(v -> saveProgress());
        btnViewAchievements.setOnClickListener(v -> viewProgress());
    }

    private void loadSavedData() {
        sessionCount = prefs.getInt("session_count", 0);
        totalMinutes = prefs.getInt("total_minutes", 0);
        updateSessionSummary();
    }

    private void startTimer() {
        if (!isRunning) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    updateTimerText();
                }

                @Override
                public void onFinish() {
                    isRunning = false;
                    tvTimerStatus.setText("Session completed 🎉");
                    sessionCount++;
                    totalMinutes += currentDuration;

                    // Save to persistent storage
                    saveToPersistentStorage();

                    // Update streak
                    updateStreak();

                    // Update longest session
                    updateLongestSession(currentDuration);

                    updateSessionSummary();
                    resetTimer();
                }
            }.start();

            isRunning = true;
            tvTimerStatus.setText("In progress... 🐝");
        }
    }

    private void pauseTimer() {
        if (isRunning && countDownTimer != null) {
            countDownTimer.cancel();
            isRunning = false;
            tvTimerStatus.setText("Paused ⏸️");
        }
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        timeLeftInMillis = currentDuration * 60 * 1000;
        updateTimerText();
        tvTimerStatus.setText("Ready to start 🐝");
    }

    private void setDuration(int minutes) {
        if (!isRunning) {
            currentDuration = minutes;
            timeLeftInMillis = minutes * 60 * 1000;
            updateTimerText();
            tvTimerStatus.setText("Duration: " + minutes + " minutes");
        }
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateSessionSummary() {
        tvSessionCount.setText(String.valueOf(sessionCount));
        tvTotalTime.setText(String.valueOf(totalMinutes));
    }

    private void saveProgress() {
        saveToPersistentStorage();
        tvTimerStatus.setText("Progress saved ✅");
    }

    private void saveToPersistentStorage() {
        // Save current session data
        prefs.edit()
                .putInt("session_count", sessionCount)
                .putInt("total_minutes", totalMinutes)
                .apply();

        // Save to global statistics
        int totalSessions = prefs.getInt("total_sessions", 0);
        int totalStudyTime = prefs.getInt("total_study_time", 0);

        prefs.edit()
                .putInt("total_sessions", totalSessions + sessionCount)
                .putInt("total_study_time", totalStudyTime + totalMinutes)
                .apply();

        // Update total study days
        int totalDays = prefs.getInt("total_study_days", 0);
        prefs.edit().putInt("total_study_days", totalDays + 1).apply();
    }

    private void updateStreak() {
        long today = getTodayInMillis();
        long lastDate = prefs.getLong("last_study_date", 0);

        if (lastDate == 0) {
            // First time studying
            prefs.edit()
                    .putInt("current_streak", 1)
                    .putInt("max_streak", 1)
                    .putLong("last_study_date", today)
                    .apply();
            return;
        }

        long daysDiff = (today - lastDate) / (1000 * 60 * 60 * 24);
        int currentStreak = prefs.getInt("current_streak", 0);
        int maxStreak = prefs.getInt("max_streak", 0);

        if (daysDiff == 0) {
            // Same day, don't change streak
            return;
        } else if (daysDiff == 1) {
            // Consecutive day, increment streak
            currentStreak++;
            maxStreak = Math.max(maxStreak, currentStreak);
            prefs.edit()
                    .putInt("current_streak", currentStreak)
                    .putInt("max_streak", maxStreak)
                    .putLong("last_study_date", today)
                    .apply();
        } else {
            // Streak broken, reset to 1
            prefs.edit()
                    .putInt("current_streak", 1)
                    .putLong("last_study_date", today)
                    .apply();
        }
    }

    private void updateLongestSession(int sessionDuration) {
        int longestSession = prefs.getInt("longest_session", 0);
        if (sessionDuration > longestSession) {
            prefs.edit().putInt("longest_session", sessionDuration).apply();
        }
    }

    private long getTodayInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private void viewProgress() {
        // Navigate to AchievementsActivity
        // The activity will automatically load all data from SharedPreferences and TaskController
        Intent intent = new Intent(this, AchievementsActivity.class);
        startActivity(intent);
    }

    private int calculateStreak() {
        long lastStudyDate = prefs.getLong("last_study_date", 0);
        long today = getTodayInMillis();

        if (lastStudyDate == 0) return 0;

        long daysDifference = (today - lastStudyDate) / (1000 * 60 * 60 * 24);

        if (daysDifference <= 1) {
            return prefs.getInt("current_streak", 0);
        } else {
            return 0;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save data when leaving the activity
        saveToPersistentStorage();
    }
}