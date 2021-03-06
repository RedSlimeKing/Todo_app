package com.example.to_doapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

public class TaskListActivity extends AppCompatActivity {

    private TaskList mTaskList;
    private int mPosition;
    private static boolean mHideCompleted;
    private EditText mListName;
    private InputMethodManager imm;
    private Switch hideSwitch;

    private RecyclerView mRecyclerview;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_layout);

        //Read the list from the intent:
        mTaskList = (TaskList) getIntent().getSerializableExtra("Card");
        mPosition = getIntent().getIntExtra("APosition", 0);

        this.mHideCompleted = mTaskList.isHidden();

        mListName = findViewById(R.id.title_text);
        mListName.setText(mTaskList.getListName());

        // Get keyboard Reference
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        // On enter pressed hide keyboard
        mListName.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&  (keyCode == KeyEvent.KEYCODE_ENTER)) {
                mListName.clearFocus();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                return true;
            }
            return false;
        });

        // On Focus change hide keyboard
        mListName.setOnFocusChangeListener((view, hasFocus) -> {
            if(!hasFocus){
                mListName.clearFocus();
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        hideSwitch = findViewById(R.id.switch_comp);
        hideSwitch.setChecked(mHideCompleted);

        hideSwitch.getTrackDrawable().setColorFilter(Color.parseColor("#595251"), PorterDuff.Mode.SRC_IN);

        hideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TaskListActivity.setHideCompleted(isChecked);
                if(mHideCompleted) {
                    for (int pos = 0; pos < mAdapter.getItemCount(); pos++) {
                        TaskAdapter.TaskHolder holder = (TaskAdapter.TaskHolder) mRecyclerview.findViewHolderForAdapterPosition(pos);
                        if(holder != null){
                            if (holder.mCheckBox.isChecked()) {
                                holder.hide();
                            }
                        }
                    }
                } else {
                    for (int pos = 0; pos < mAdapter.getItemCount(); pos++) {
                        TaskAdapter.TaskHolder holder = (TaskAdapter.TaskHolder) mRecyclerview.findViewHolderForAdapterPosition(pos);
                        if(holder != null) holder.show();
                    }

                }
            }
        });

        SetupRecyclerview();

        if(mTaskList.getTaskList().size() <= 0){
            mTaskList.getTaskList().add(new Task("", false));
            mAdapter.notifyDataSetChanged();
        }
    }

    public void SetupRecyclerview(){
        mRecyclerview = findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);

        mAdapter = new TaskAdapter(mTaskList.getTaskList(), this, findViewById(R.id.screen), mRecyclerview);
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(mLayoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeHelperList(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

        // Stop from drawing line at bottom of recyclerView
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        mAdapter.notifyDataSetChanged();

        ItemTouchHelper iTH = new ItemTouchHelper(simpleCallback);
        iTH.attachToRecyclerView(mRecyclerview);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if(fromPosition == recyclerView.getAdapter().getItemCount() - 1){
                return false;
            }

            if(toPosition == recyclerView.getAdapter().getItemCount() - 1){
                toPosition -= 1;
            }

            Collections.swap(mTaskList.getTaskList(), fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState == ACTION_STATE_DRAG) {
                viewHolder.itemView.setAlpha(0.5f);
            }
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setAlpha(1.0f);
        }
    };

    public static void setHideCompleted(boolean mHideCompleted) {
        TaskListActivity.mHideCompleted = mHideCompleted;
    }

    public static boolean getHideCompleted() {
        return TaskListActivity.mHideCompleted;
    }

    @Override
    public void onStop(){
        super.onStop();
        mTaskList.setHidden(mHideCompleted);
        mTaskList.setListName(mListName.getText().toString());
        MainActivity.Save(mPosition, mTaskList);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTaskList.setHidden(mHideCompleted);
        mTaskList.setListName(mListName.getText().toString());
        MainActivity.Save(mPosition, mTaskList);
    }

    @Override
    public void onBackPressed() {
        mTaskList.setListName(mListName.getText().toString());
        mTaskList.setHidden(mHideCompleted);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("returnCard", mTaskList);
        resultIntent.putExtra("APos", mPosition);
        setResult(1, resultIntent);
        super.onBackPressed();
    }
}
