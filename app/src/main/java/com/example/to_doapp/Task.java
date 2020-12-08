package com.example.to_doapp;

import java.io.Serializable;

public class Task implements Serializable {
    private boolean mIsCompleted;
    private String mTaskDesc;

    public Task(String taskDesc, boolean isCompleted) {
        this.mIsCompleted = isCompleted;
        this.mTaskDesc = taskDesc;
    }

    public String getTaskDesc() {
        return mTaskDesc;
    }

    public void setTaskDesc(String mTaskDesc) {
        this.mTaskDesc = mTaskDesc;
    }

    public boolean getisIsCompleted() {
        return mIsCompleted;
    }

    public void setIsCompleted(boolean mIsCompleted) {
        this.mIsCompleted = mIsCompleted;
    }
}
