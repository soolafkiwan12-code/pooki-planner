package com.example.pookiplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pookiplanner.PookiPlannerApplication;
import com.example.pookiplanner.R;
import com.example.pookiplanner.activities.AddTaskActivity;
import com.example.pookiplanner.adapters.TaskAdapter;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.StudyTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TasksFragment extends Fragment implements TaskAdapter.Listener {

    private DatabaseHelper db;
    private TaskAdapter adapter;
    private TextView emptyText;
    private RecyclerView recycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(requireContext());
        emptyText = view.findViewById(R.id.textEmptyTasks);
        recycler = view.findViewById(R.id.recyclerTasks);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TaskAdapter(this);
        recycler.setAdapter(adapter);
        view.findViewById(R.id.fabAddTask).setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddTaskActivity.class)));
        loadTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        long uid = ((PookiPlannerApplication) requireActivity().getApplication())
                .getPrefManager().getUserId();
        adapter.setTasks(db.getTasksForUser(uid));
        boolean empty = adapter.getItemCount() == 0;
        emptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
        recycler.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onComplete(StudyTask task, boolean completed) {
        db.updateTaskCompleted(task.getId(), completed);
        loadTasks();
    }

    @Override
    public void onDelete(StudyTask task) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.delete_task_title)
                .setMessage(R.string.delete_task_message)
                .setPositiveButton(R.string.yes, (d, w) -> {
                    db.deleteTask(task.getId());
                    loadTasks();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
