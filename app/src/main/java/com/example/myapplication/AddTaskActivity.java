package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.controller.TaskController;
import com.example.myapplication.model.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private TextInputEditText editTaskTitle, editTaskDescription;
    private RadioGroup radioPriority;
    private TextView tvDueDate;
    private MaterialButton btnAdd;
    private Calendar dueDateCalendar;
    private boolean isEditMode = false;
    private int editPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // Check if in edit mode
        if (getIntent().hasExtra("MODE") && "EDIT".equals(getIntent().getStringExtra("MODE"))) {
            isEditMode = true;
            editPosition = getIntent().getIntExtra("POSITION", -1);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? "Edit Task" : "Add New Task");
        }

        initializeViews();
        setupDueDatePicker();

        if (isEditMode) loadTaskData();

        setupAddButton();
    }

    private void initializeViews() {
        editTaskTitle = findViewById(R.id.editTaskTitle);
        editTaskDescription = findViewById(R.id.editTaskDescription);
        radioPriority = findViewById(R.id.radioPriority);
        tvDueDate = findViewById(R.id.tvDueDate);
        btnAdd = findViewById(R.id.btnAdd);
        dueDateCalendar = Calendar.getInstance();

        if (isEditMode) {
            btnAdd.setText("Update Task");
        }
    }

    private void loadTaskData() {
        editTaskTitle.setText(getIntent().getStringExtra("TITLE"));
        editTaskDescription.setText(getIntent().getStringExtra("DESCRIPTION"));

        String priority = getIntent().getStringExtra("PRIORITY");
        if (priority != null) {
            switch (priority) {
                case "High": radioPriority.check(R.id.priorityHigh); break;
                case "Medium": radioPriority.check(R.id.priorityMedium); break;
                default: radioPriority.check(R.id.priorityLow); break;
            }
        }

        String dueDate = getIntent().getStringExtra("DUE_DATE");
        if (dueDate != null && !"No due date".equals(dueDate)) {
            tvDueDate.setText("Due Date: " + dueDate);
        }
    }

    private void setupDueDatePicker() {
        tvDueDate.setOnClickListener(v -> {
            int year = dueDateCalendar.get(Calendar.YEAR);
            int month = dueDateCalendar.get(Calendar.MONTH);
            int day = dueDateCalendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddTaskActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        dueDateCalendar.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        tvDueDate.setText("Due Date: " + sdf.format(dueDateCalendar.getTime()));
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });
    }

    private void setupAddButton() {
        btnAdd.setOnClickListener(v -> {
            String title = editTaskTitle.getText() != null ? editTaskTitle.getText().toString().trim() : "";
            String description = editTaskDescription.getText() != null ? editTaskDescription.getText().toString().trim() : "";

            if (title.length() < 3) {
                Toast.makeText(this, "❌ Task title must be at least 3 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedId = radioPriority.getCheckedRadioButtonId();
            RadioButton selectedPriority = findViewById(selectedId);
            String priority = selectedPriority.getText().toString();

            String dueDate = tvDueDate.getText().toString().contains("Due Date:")
                    ? tvDueDate.getText().toString().replace("Due Date: ", "")
                    : "No due date";

            if (isEditMode && editPosition >= 0) {
                TaskController.updateTask(editPosition, title, description, priority, dueDate, this);
                Toast.makeText(this, "✅ Task updated!", Toast.LENGTH_SHORT).show();
            } else {
                TaskController.addTask(title, description, priority, dueDate, this);
                Toast.makeText(this, "✅ Task added!", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
