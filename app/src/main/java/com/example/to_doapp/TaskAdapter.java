package com.example.to_doapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskAdapter extends  RecyclerView.Adapter<TaskAdapter.TaskHolder>{
    private ArrayList<Task> mList;
    private View mActivity;
    private Context mContext;

    private Task mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public TaskAdapter(ArrayList<Task> list, Context context, View activity){
        this.mList = list;
        this.mActivity = activity;
        this.mContext = context;
    }

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

    public Context getContext(){
        return mContext;
    }

    public void toggleCheck(int position){
        Task lTask = mList.get(position);
        lTask.setIsCompleted(!lTask.getisIsCompleted());
        mList.add(position, lTask);
        notifyItemChanged(position);
    }

    public void deleteItem(int position) {
        mRecentlyDeletedItem = mList.get(position);
        mRecentlyDeletedItemPosition = position;
        mList.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = mActivity;
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());

        snackbar.show();
    }

    private void undoDelete() {
        mList.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
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
