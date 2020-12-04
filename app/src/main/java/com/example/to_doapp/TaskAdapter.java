package com.example.to_doapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends  RecyclerView.Adapter<TaskAdapter.TaskHolder>{
    private ArrayList<Task> mList;
    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new TaskAdapter.TaskHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task lTask = mList.get(position);
        holder.mEditText.setText(lTask.getTaskDesc());
        holder.mCheckBox.setChecked(lTask.getisIsCompleted());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class TaskHolder extends RecyclerView.ViewHolder{
        private CheckBox mCheckBox;
        private EditText mEditText;
        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            this.mCheckBox = itemView.findViewById(R.id.checkBox);
            this.mEditText = itemView.findViewById(R.id.editText);
        }

        public CheckBox getCheckBox() {
            return mCheckBox;
        }

        public EditText getEditText() {
            return mEditText;
        }
    }
}
