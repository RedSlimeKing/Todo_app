package com.example.to_doapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListActivity extends AppCompatActivity {

    private TaskList mTaskList;
    private int mPosition;
    private static boolean mHideCompleted;
    private EditText mListName;
    private InputMethodManager imm;

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

        SetupRecyclerview();

        if(mTaskList.getTaskList().size() <= 0){
            mTaskList.getTaskList().add(new Task("", false));
            mAdapter.notifyDataSetChanged();
        }
    }

    public void SetupRecyclerview(){
        mRecyclerview = findViewById(R.id.recyclerview);
        mRecyclerview.setHasFixedSize(true);

        mAdapter = new TaskAdapter(mTaskList.getTaskList(), this, findViewById(R.id.screen));
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
    };

    public static void setmHideCompleted(boolean mHideCompleted) {
        TaskListActivity.mHideCompleted = mHideCompleted;
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
