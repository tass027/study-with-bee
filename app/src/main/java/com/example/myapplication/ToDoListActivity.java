package com.example.myapplication;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.controller.TaskController;
import com.example.myapplication.model.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ToDoListActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private LinearLayout emptyState, weekDaysContainer;
    private ImageView beeIllustration;
    private TextView tvDate, tvTitle;
    private ImageButton btnBack;
    private MaterialButton btnModifyTask, btnDeleteTask, btnClearAll;
    private FloatingActionButton fabAddTask;
    private TaskAdapter taskAdapter;
    private Calendar selectedDate;
    private int selectedTaskPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        selectedDate = Calendar.getInstance();
        initializeViews();
        setupWeekDays();
        updateDate();
        refreshTaskList();
        animateBee();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvDate = findViewById(R.id.tvDate);
        weekDaysContainer = findViewById(R.id.weekDaysContainer);
        rvTasks = findViewById(R.id.rvTasks);
        emptyState = findViewById(R.id.emptyState);
        beeIllustration = findViewById(R.id.beeIllustration);
        fabAddTask = findViewById(R.id.fabAddTask);
        btnModifyTask = findViewById(R.id.btnModifyTask);
        btnDeleteTask = findViewById(R.id.btnDeleteTask);
        btnClearAll = findViewById(R.id.btnClearAll);

        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish());
        fabAddTask.setOnClickListener(v -> startActivity(new Intent(this, AddTaskActivity.class)));
        btnModifyTask.setOnClickListener(v -> modifySelectedTask());
        btnDeleteTask.setOnClickListener(v -> deleteSelectedTask());
        btnClearAll.setOnClickListener(v -> clearAllTasks());
    }

    private void setupWeekDays() {
        weekDaysContainer.removeAllViews();
        Calendar calendar = (Calendar) selectedDate.clone();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.DAY_OF_MONTH, -7);

        for (int i = 0; i < 12; i++) {
            View dayView = createModernDayView(calendar);
            weekDaysContainer.addView(dayView);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private View createModernDayView(Calendar calendar) {
        LinearLayout dayContainer = new LinearLayout(this);
        dayContainer.setOrientation(LinearLayout.VERTICAL);
        dayContainer.setGravity(Gravity.CENTER);
        dayContainer.setPadding(12, 16, 12, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                (int) (60 * getResources().getDisplayMetrics().density),
                (int) (75 * getResources().getDisplayMetrics().density)
        );
        params.setMargins(6, 0, 6, 0);
        dayContainer.setLayoutParams(params);

        Calendar today = Calendar.getInstance();
        boolean isToday = calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR);
        boolean isSelected = calendar.get(Calendar.DAY_OF_YEAR) == selectedDate.get(Calendar.DAY_OF_YEAR) &&
                calendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR);

        dayContainer.setBackgroundResource(isSelected ? R.drawable.day_selected_modern : R.drawable.day_unselected_modern);

        TextView tvMonth = new TextView(this);
        tvMonth.setText(new SimpleDateFormat("MMM", Locale.getDefault()).format(calendar.getTime()));
        tvMonth.setTextSize(10);
        tvMonth.setTextColor(isSelected ? 0xFFFFFFFF : 0xFF8B7355);
        tvMonth.setGravity(Gravity.CENTER);
        tvMonth.setAllCaps(true);
        dayContainer.addView(tvMonth);

        TextView tvDay = new TextView(this);
        tvDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        tvDay.setTextSize(20);
        tvDay.setTextColor(isSelected ? 0xFFFFFFFF : 0xFF3D2E1F);
        tvDay.setTypeface(null, android.graphics.Typeface.BOLD);
        tvDay.setGravity(Gravity.CENTER);
        dayContainer.addView(tvDay);

        TextView tvDayName = new TextView(this);
        String dayName = new SimpleDateFormat("E", Locale.getDefault()).format(calendar.getTime());
        tvDayName.setText(dayName.substring(0, 2));
        tvDayName.setTextSize(10);
        tvDayName.setTextColor(isSelected ? 0xFFFFFFFF : 0xFF8B7355);
        tvDayName.setGravity(Gravity.CENTER);
        dayContainer.addView(tvDayName);

        if (isToday && !isSelected) {
            View todayDot = new View(this);
            LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(
                    (int) (6 * getResources().getDisplayMetrics().density),
                    (int) (6 * getResources().getDisplayMetrics().density));
            dotParams.setMargins(0, 4, 0, 0);
            todayDot.setLayoutParams(dotParams);
            todayDot.setBackgroundResource(R.drawable.today_dot);
            dayContainer.addView(todayDot);
        }

        final Calendar dayCalendar = (Calendar) calendar.clone();
        dayContainer.setOnClickListener(v -> {
            selectedDate = dayCalendar;
            setupWeekDays();
            updateDate();
            refreshTaskList();
        });

        return dayContainer;
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(selectedDate.getTime()));

        Calendar today = Calendar.getInstance();
        tvTitle.setText(selectedDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) &&
                selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) ? "Today's Tasks" : "Tasks");
    }

    private void refreshTaskList() {
        ArrayList<Task> tasks = TaskController.getTasks(this);

        if (tasks == null || tasks.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvTasks.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvTasks.setVisibility(View.VISIBLE);

            taskAdapter = new TaskAdapter(tasks, new TaskAdapter.OnTaskInteractionListener() {
                @Override
                public void onTaskClick(int position) {
                    selectedTaskPosition = position;
                    taskAdapter.setSelectedPosition(position);
                    taskAdapter.notifyDataSetChanged();
                    Toast.makeText(ToDoListActivity.this, "Task selected", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onTaskCheckChanged(int position, boolean isChecked) {
                    TaskController.toggleTaskCompletion(position, ToDoListActivity.this);
                    taskAdapter.notifyItemChanged(position);
                }
            });
            rvTasks.setAdapter(taskAdapter);
        }
    }

    private void modifySelectedTask() {
        if (selectedTaskPosition < 0) {
            Toast.makeText(this, "⚠️ Please select a task to modify", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = TaskController.getTask(selectedTaskPosition, this);
        if (task == null) return;

        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra("MODE", "EDIT");
        intent.putExtra("POSITION", selectedTaskPosition);
        intent.putExtra("TITLE", task.getTitle());
        intent.putExtra("DESCRIPTION", task.getDescription());
        intent.putExtra("PRIORITY", task.getPriority());
        intent.putExtra("DUE_DATE", task.getDueDate());
        startActivity(intent);
    }

    private void deleteSelectedTask() {
        if (selectedTaskPosition < 0) {
            Toast.makeText(this, "⚠️ Please select a task to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        showDeleteTaskDialog(selectedTaskPosition);
    }

    private void clearAllTasks() {
        ArrayList<Task> tasks = TaskController.getTasks(this);
        if (tasks.isEmpty()) {
            Toast.makeText(this, "No tasks to clear!", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("🗑️ Clear All Tasks")
                .setMessage("Are you sure you want to delete all " + tasks.size() + " tasks?")
                .setPositiveButton("Yes, Clear All", (dialog, which) -> {
                    TaskController.clearTasks(this);
                    selectedTaskPosition = -1;
                    refreshTaskList();
                    Toast.makeText(this, "✅ All tasks cleared!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteTaskDialog(int position) {
        Task task = TaskController.getTask(position, this);
        if (task == null) return;

        new AlertDialog.Builder(this)
                .setTitle("🗑️ Delete Task")
                .setMessage("Are you sure you want to delete this task?\n\n\"" + task.getTitle() + "\"")
                .setPositiveButton("Delete", (dialog, which) -> {
                    TaskController.removeTask(position, this);
                    selectedTaskPosition = -1;
                    refreshTaskList();
                    Toast.makeText(this, "✅ Task deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void animateBee() {
        if (beeIllustration != null) {
            beeIllustration.animate()
                    .translationY(-15f)
                    .setDuration(1500)
                    .withEndAction(() -> beeIllustration.animate()
                            .translationY(0f)
                            .setDuration(1500)
                            .withEndAction(this::animateBee)
                            .start())
                    .start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedTaskPosition = -1;
        refreshTaskList();
    }

    // --------- TASK ADAPTER ---------
    private static class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private final ArrayList<Task> tasks;
        private final OnTaskInteractionListener listener;
        private int selectedPosition = -1;

        interface OnTaskInteractionListener {
            void onTaskClick(int position);
            void onTaskCheckChanged(int position, boolean isChecked);
        }

        TaskAdapter(ArrayList<Task> tasks, OnTaskInteractionListener listener) {
            this.tasks = tasks;
            this.listener = listener;
        }

        public void setSelectedPosition(int position) {
            this.selectedPosition = position;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            holder.bind(tasks.get(position), position, position == selectedPosition);
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        class TaskViewHolder extends RecyclerView.ViewHolder {
            android.widget.CheckBox cbTask;
            TextView tvTaskTitle, tvTaskDescription, tvPriority, tvDueDate, tvPriorityEmoji;
            View selectionIndicator;

            TaskViewHolder(View itemView) {
                super(itemView);
                cbTask = itemView.findViewById(R.id.cbTask);
                tvTaskTitle = itemView.findViewById(R.id.tvTaskTitle);
                tvTaskDescription = itemView.findViewById(R.id.tvTaskDescription);
                tvPriority = itemView.findViewById(R.id.tvPriority);
                tvDueDate = itemView.findViewById(R.id.tvDueDate);
                tvPriorityEmoji = itemView.findViewById(R.id.tvPriorityEmoji);
                selectionIndicator = itemView.findViewById(R.id.selectionIndicator);
            }

            void bind(Task task, int position, boolean isSelected) {
                tvTaskTitle.setText(task.getTitle());
                tvPriorityEmoji.setText(task.getPriorityEmoji());

                if (task.getDescription() != null && !task.getDescription().isEmpty()) {
                    tvTaskDescription.setText(task.getDescription());
                    tvTaskDescription.setVisibility(View.VISIBLE);
                } else {
                    tvTaskDescription.setVisibility(View.GONE);
                }

                tvPriority.setText(task.getPriority());
                tvPriority.setBackgroundTintList(android.content.res.ColorStateList.valueOf(task.getPriorityColor()));
                tvDueDate.setText("📅 " + task.getDueDate());
                selectionIndicator.setVisibility(isSelected ? View.VISIBLE : View.GONE);

                // Prevent recycled view issues
                cbTask.setOnCheckedChangeListener(null);
                cbTask.setChecked(task.isCompleted());
                updateStrikeThrough(task.isCompleted());

                cbTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    listener.onTaskCheckChanged(position, isChecked);
                    updateStrikeThrough(isChecked);
                });

                itemView.setOnClickListener(v -> listener.onTaskClick(position));
            }

            private void updateStrikeThrough(boolean isCompleted) {
                if (isCompleted) {
                    tvTaskTitle.setPaintFlags(tvTaskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvTaskTitle.setAlpha(0.6f);
                    tvTaskDescription.setAlpha(0.6f);
                } else {
                    tvTaskTitle.setPaintFlags(tvTaskTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                    tvTaskTitle.setAlpha(1.0f);
                    tvTaskDescription.setAlpha(1.0f);
                }
            }
        }
    }
}
