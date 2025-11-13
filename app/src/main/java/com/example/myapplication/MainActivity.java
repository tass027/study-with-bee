package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private TextView tvUserName;
    private MaterialButton btnLectureNotes, btnStudyTimer, btnToDoList, btnAchievements;
    private ImageView beeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize all views
        initializeViews();

        // Load and display stored username
        loadUserData();

        // Setup button click listeners
        setupClickListeners();

        // Animate the bee icon
        startBeeAnimation();
    }

    /** Initialize all the view components **/
    private void initializeViews() {
        tvUserName = findViewById(R.id.tvUserName);
        btnLectureNotes = findViewById(R.id.btnLectureNotes);
        btnStudyTimer = findViewById(R.id.btnStudyTimer);
        btnToDoList = findViewById(R.id.btnToDoList);
        btnAchievements = findViewById(R.id.btnAchievements);
        beeIcon = findViewById(R.id.beeIcon);
    }

    /** Load the saved username from SharedPreferences **/
    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = prefs.getString("userName", "Student"); // Default value if not saved
        tvUserName.setText(userName);
    }

    /** Set click listeners for all buttons **/
    private void setupClickListeners() {
        btnLectureNotes.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, LectureNotesActivity.class)));

        btnStudyTimer.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, StudyTimerActivity.class)));

        btnToDoList.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ToDoListActivity.class)));

        btnAchievements.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AchievementsActivity.class)));

        // Add click behavior for bee icon
        beeIcon.setOnClickListener(v -> showBeeMessage());
    }

    /** Start buzzing animation for the bee icon **/
    private void startBeeAnimation() {
        Animation buzz = AnimationUtils.loadAnimation(this, R.anim.buzz);
        beeIcon.startAnimation(buzz);
    }

    /** Small popup message when clicking the bee **/
    private void showBeeMessage() {
        Toast.makeText(this, "Bee-lieve in yourself! 🐝✨", Toast.LENGTH_SHORT).show();
    }
}
