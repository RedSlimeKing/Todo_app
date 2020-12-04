package com.example.to_doapp;

public class Task {
    private boolean mIsCompleted;
    private String mTaskDesc;

    public Task(boolean isCompleted, String taskDesc) {
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
