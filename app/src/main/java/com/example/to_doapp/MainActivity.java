package com.example.to_doapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

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

        SetupRecyclerview();
    }

    public void SetupRecyclerview(){
        mRecyclerview = findViewById(R.id.mainRecyclerView);
        mRecyclerview.setHasFixedSize(true);

        ArrayList<String> lists = new ArrayList();
        for(int i = 0; i < mLists.size(); i++){
            lists.add(mLists.get(i).getListName());
        }

        mAdapter = new ListAdapter(lists, this, findViewById(android.R.id.content).getRootView());
        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerview.setAdapter(mAdapter);
        mRecyclerview.setLayoutManager(mLayoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeHelperMain(mAdapter));
        itemTouchHelper.attachToRecyclerView(mRecyclerview);

        mAdapter.notifyDataSetChanged();
    }
}