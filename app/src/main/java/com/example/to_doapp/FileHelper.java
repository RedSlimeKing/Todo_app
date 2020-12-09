package com.example.to_doapp;

/* File systems */
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class FileHelper {

    private static final String filename = "StoredTask.txt";

    public static void WriteData(Context context, ArrayList<TaskList> ls){
        FileOutputStream fos;
        ArrayList<String> saveData = new ArrayList<>();
        if(ls.size() <= 0){
            return;
        }
        for(int i = 0; i < ls.size(); i++){
            TaskList ci = ls.get(i);
            String item = "ListName" + "[|]" + ci.getListName();
            saveData.add(item);
            item = "HideCompleted" + "[|]" + ci.isHidden();
            saveData.add(item);
            for(Task ti : ci.getTaskList()){
                item = ti.getTaskDesc() + "[|]" + ti.getIsCompleted();
                saveData.add(item);
            }
        }

        try{
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(saveData);
            oos.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public static ArrayList<TaskList> LoadTask(Context context){
        ArrayList<TaskList> TaskLists = new ArrayList<>();
        FileInputStream fis = null;

        try{
            ArrayList<String> items;
            fis = context.openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            items = (ArrayList<String>) ois.readObject();
            int index = -1;
            for(int i = 0; i < items.size(); i++){
                String[] parts = items.get(i).split("[|]");
                String part1 = "", part2 = "";
                part1 = parts[0].substring(0, parts[0].length() - 1);
                part2 = parts[1].substring(1);


                if(part1.equals("ListName")){
                    index++;
                    TaskLists.add(new TaskList(part2, new ArrayList<>()));
                } else if(part1.equals("HideCompleted")){
                    TaskLists.get(index).setHidden(Boolean.parseBoolean(part2));
                } else {
                    TaskLists.get(index).addTaskItem(new Task(part1, Boolean.parseBoolean(part2)));
                }
            }
        } catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        } finally{
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return TaskLists;
    }
}
