package com.example.to_doapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    // array of task lists
    private ArrayList<TaskList> mLists;

    // RecyclerView setup variables
    private RecyclerView mRecyclerview;
    private ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLists = new ArrayList<>();

        mLists.add(new TaskList("Test", new ArrayList<>()));
        mLists.add(new TaskList("Test 2", new ArrayList<>()));
        mLists.add(new TaskList("Test 3", new ArrayList<>()));
        mLists.add(new TaskList("Test 4", new ArrayList<>()));
        mLists.add(new TaskList("Test 5", new ArrayList<>()));

        SetupRecyclerview();
    }

    public void SetupRecyclerview(){
        mRecyclerview = findViewById(R.id.mainRecyclerView);
        mRecyclerview.setHasFixedSize(true);

        mAdapter = new ListAdapter(mLists, this, findViewById(R.id.screen));
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(mLayoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeHelperMain(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

        //Stop from drawing line at bottom of recyclerView
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        // On ViewHolder click
        mAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion) {

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

            Collections.swap(mLists, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };
}