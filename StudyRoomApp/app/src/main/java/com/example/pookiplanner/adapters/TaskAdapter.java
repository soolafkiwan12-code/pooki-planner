package com.example.pookiplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pookiplanner.R;
import com.example.pookiplanner.models.StudyTask;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.Holder> {

    public interface Listener {
        void onComplete(StudyTask task, boolean completed);
        void onDelete(StudyTask task);
    }

    private List<StudyTask> tasks = new ArrayList<>();
    private final Listener listener;

    public TaskAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setTasks(List<StudyTask> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        StudyTask t = tasks.get(position);
        h.title.setText(t.getTitle());
        h.desc.setText(t.getDescription() == null || t.getDescription().isEmpty()
                ? "—" : t.getDescription());
        h.due.setText(t.getDueDate());
        h.check.setOnCheckedChangeListener(null);
        h.check.setChecked(t.isCompleted());
        h.title.setAlpha(t.isCompleted() ? 0.5f : 1f);
        h.check.setOnCheckedChangeListener((b, checked) -> listener.onComplete(t, checked));
        h.delete.setOnClickListener(v -> listener.onDelete(t));
    }

    @Override
    public int getItemCount() { return tasks.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView title, desc, due;
        CheckBox check;
        ImageButton delete;
        Holder(View v) {
            super(v);
            title = v.findViewById(R.id.textTaskTitle);
            desc = v.findViewById(R.id.textTaskDescription);
            due = v.findViewById(R.id.textTaskDueDate);
            check = v.findViewById(R.id.checkboxTask);
            delete = v.findViewById(R.id.btnDeleteTask);
        }
    }
}
