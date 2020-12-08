package com.example.to_doapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    // array of task lists
    private static ArrayList<TaskList> mLists;

    // RecyclerView setup variables
    private RecyclerView mRecyclerview;
    private static ListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageButton addButton;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLists = FileHelper.LoadTask(this);

        mContext = this;

        SetupRecyclerview();

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLists.add(new TaskList("New List", new ArrayList<>()));
                mAdapter.notifyDataSetChanged();
                //Open list
                LoadCard(mLists.size() - 1);
            }
        });
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

        // Stop from drawing line at bottom of recyclerView
        mRecyclerview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });

        // On ViewHolder click
        mAdapter.setOnItemClickListener(postion -> LoadCard(postion));

        mAdapter.notifyDataSetChanged();

        ItemTouchHelper iTH = new ItemTouchHelper(simpleCallback);
        iTH.attachToRecyclerView(mRecyclerview);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0)     {
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

    public static void Save(int position, TaskList ci){
        mLists.set(position, ci);
        mAdapter.notifyDataSetChanged();
        FileHelper.WriteData(mContext, mLists);
    }

    public void LoadCard(int position){
        TaskList item = mLists.get(position);
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("Card", item);
        intent.putExtra("APosition", position);

        startActivityForResult(intent, 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FileHelper.WriteData(this, mLists);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == 1){
                TaskList ci = (TaskList) data.getSerializableExtra("returnCard");
                int Pos = data.getIntExtra("APos", 0);
                mLists.set(Pos, ci);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}