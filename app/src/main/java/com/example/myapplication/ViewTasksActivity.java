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
import com.example.myapplication.model.Task;

import java.util.ArrayList;

public class ViewTasksActivity extends AppCompatActivity {

    ListView listView;
    TextView tvEmpty;
    ArrayAdapter<Task> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tasks);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Tasks");
        }

        listView = findViewById(R.id.listView);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Load tasks
        TaskController.loadTasks(this);

        setupListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void setupListView() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Task task = TaskController.getTasks(this).get(position);
            Toast.makeText(this, "📝 " + task, Toast.LENGTH_SHORT).show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteDialog(position);
            return true;
        });
    }

    private void refreshList() {
        ArrayList<Task> tasks = TaskController.getTasks(this);

        if (tasks.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
            listView.setAdapter(adapter);
        }
    }

    private void showDeleteDialog(int position) {
        Task task = TaskController.getTasks(this).get(position);
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Delete Task")
                .setMessage("Delete this task?\n\"" + task + "\"")
                .setPositiveButton("Delete", (dialog, which) -> {
                    TaskController.removeTask(position, this);
                    refreshList();
                    Toast.makeText(this, "✅ Task deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}