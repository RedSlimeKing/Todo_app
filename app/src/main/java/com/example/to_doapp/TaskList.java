package com.example.to_doapp;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskList implements Serializable {
    private String mListName;
    private ArrayList<Task> mTaskList;
    private boolean mIsHidden;

    public TaskList(String name, ArrayList<Task> list) {
        this.mListName = name;
        this.mTaskList = list;
        this.mIsHidden = false;
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

    public boolean isHidden() {
        return mIsHidden;
    }

    public void setHidden(boolean hidden) {
        this.mIsHidden = hidden;
    }
}
