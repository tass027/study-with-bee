package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myapplication.controller.TaskController;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class RemindersActivity extends AppCompatActivity {

    private TextInputEditText etReminder;
    private ListView lvReminders;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Reminders");
        }

        initializeViews();
        setupRemindersList();
    }

    private void initializeViews() {
        etReminder = findViewById(R.id.etReminder);
        MaterialButton btnAddReminder = findViewById(R.id.btnAddReminder);
        lvReminders = findViewById(R.id.lvReminders);
        tvEmpty = findViewById(R.id.tvEmpty);

        btnAddReminder.setOnClickListener(v -> addNewReminder());
    }

    private void setupRemindersList() {
        lvReminders.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteReminderDialog(position);
            return true;
        });

        refreshRemindersList();
    }

    private void addNewReminder() {
        String reminder = etReminder.getText() != null ? etReminder.getText().toString().trim() : "";
        if (reminder.length() < 3) {
            Toast.makeText(this, "❌ Reminder must contain at least 3 characters", Toast.LENGTH_SHORT).show();
        } else {
            TaskController.addReminder(reminder, this);
            refreshRemindersList();
            Toast.makeText(this, "✅ Reminder added: " + reminder, Toast.LENGTH_SHORT).show();
            etReminder.setText("");
        }
    }

    private void refreshRemindersList() {
        ArrayList<String> reminders = TaskController.getReminders(this);

        if (reminders.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            lvReminders.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            lvReminders.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reminders);
            lvReminders.setAdapter(adapter);
        }
    }

    private void showDeleteReminderDialog(int position) {
        String reminder = TaskController.getReminders(this).get(position);
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Delete Reminder")
                .setMessage("Delete this reminder?\n\"" + reminder + "\"")
                .setPositiveButton("Delete", (dialog, which) -> {
                    TaskController.removeReminder(position, this);
                    refreshRemindersList();
                    Toast.makeText(this, "✅ Reminder deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}