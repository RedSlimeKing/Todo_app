package com.example.to_doapp;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class TaskAdapter extends  RecyclerView.Adapter<TaskAdapter.TaskHolder>{
    private ArrayList<Task> mList;
    private View mActivity;
    private Context mContext;
    private InputMethodManager imm;

    private Task mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public TaskAdapter(ArrayList<Task> list, Context context, View activity){
        this.mList = list;
        this.mActivity = activity;
        this.mContext = context;

        // Get keyboard Reference
        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
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
        holder.mEditText.setHint("Enter Task");
        holder.mEditText.setText(lTask.getTaskDesc());
        holder.mCheckBox.setChecked(lTask.getisIsCompleted());

        // If completed but not hidden
        if(lTask.getisIsCompleted()){
            holder.mEditText.setAlpha(0.3f);
        } else {
            holder.mEditText.setAlpha(1.0f);
        }

        // If First in list is empty call for keyboard
        if(lTask.getTaskDesc().equals("") && position == 0){
            holder.mEditText.requestFocus();
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        }

        if(lTask.getTaskDesc().equals("") && position == mList.size() - 1){
            holder.mCheckBox.setVisibility(View.GONE);
        }

        holder.mCheckBox.setOnClickListener(view -> {
            lTask.setIsCompleted(holder.mCheckBox.isChecked());
            if(lTask.getisIsCompleted()){
                holder.mEditText.setAlpha(0.3f);
                holder.mCheckBox.setAlpha(0.3f);
            } else {
                holder.mEditText.setAlpha(1.0f);
                holder.mCheckBox.setAlpha(1.0f);
            }

            notifyItemChanged(position);
        });

        holder.mEditText.setOnClickListener(view -> {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        });

        holder.mEditText.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&  (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if(!holder.mEditText.getText().toString().equals("")){
                    lTask.setTaskDesc(holder.mEditText.getText().toString());
                    holder.mCheckBox.setVisibility(View.VISIBLE);
                    // Create next input
                    if(!mList.get(mList.size()-1).getTaskDesc().equals("")){
                        mList.add(new Task("",false));
                    }
                    notifyItemChanged(position);
                }
                holder.mEditText.clearFocus();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return true;
            }
            return false;
        });

        holder.mEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                lTask.setTaskDesc(holder.mEditText.getText().toString());
                // Create next input
                if(!mList.get(mList.size()-1).getTaskDesc().equals("")){
                    mList.add(new Task("",false));
                    notifyItemChanged(position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public Context getContext(){
        return mContext;
    }

    public void toggleCheck(int position){
        Task lTask = mList.get(position);
        lTask.setIsCompleted(!lTask.getisIsCompleted());
        mList.set(position, lTask);
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
        public CheckBox mCheckBox;
        public EditText mEditText;
        public RelativeLayout layout;
        public ViewGroup.LayoutParams params;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            this.mCheckBox = itemView.findViewById(R.id.checkBox);
            this.mEditText = itemView.findViewById(R.id.editText);

            layout = itemView.findViewById(R.id.parent_layout);
            params = layout.getLayoutParams();
        }

        public void hide(){
            mCheckBox.setVisibility(View.GONE);
            mEditText.setVisibility(View.GONE);
            params.height = 0;
            layout.setLayoutParams(params);
        }

        public void show(){
            mEditText.setVisibility(View.VISIBLE);
            if(mEditText.getText().toString().equals("")){
                mCheckBox.setVisibility(View.GONE);
            } else {
                mCheckBox.setVisibility(View.VISIBLE);
            }
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layout.setLayoutParams(params);
        }
    }
}
