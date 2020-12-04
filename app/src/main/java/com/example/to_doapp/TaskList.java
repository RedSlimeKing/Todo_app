package com.example.to_doapp;

import java.util.ArrayList;

public class TaskList {
    private String mListName;
    private ArrayList<Task> mTaskList;

    public TaskList(String name, ArrayList<Task> list) {
        this.mListName = name;
        this.mTaskList = list;
    }

    public String getListName() {
        return mListName;
    }

    public void setListName(String mListName) {
        this.mListName = mListName;
    }

    public ArrayList<Task> getTaskList() {
        return mTaskList;
    }

    public void setTaskList(ArrayList<Task> mTaskList) {
        this.mTaskList = mTaskList;
    }
}
