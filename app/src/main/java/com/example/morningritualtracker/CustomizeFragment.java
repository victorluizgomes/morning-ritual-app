package com.example.morningritualtracker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CustomizeFragment extends Fragment {

    private Activity containerActivity = null;
    private View inflaterView;
    private ListView ritualListView = null;
    private ArrayAdapter arrayAdapter;
    private List<String> morningRituals = new ArrayList<>();
    private HashMap<String, Boolean> ritualState;

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflaterView = inflater.inflate(R.layout.fragment_customize, container, false);
        loadRitualsFromFile();
        setUpAddButton();
        setUpRemoveLastButton();
        setupListAdapter(inflaterView);
        return inflaterView;
    }

    @Override
    public void onPause(){
        super.onPause();
        saveRitualsToFile(ritualState);
    }

    private void setUpAddButton(){
        Button button = inflaterView.findViewById(R.id.addHabit);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                addHabit();
            }
        }

        );
    }
    private void setUpRemoveLastButton(){
        Button button = inflaterView.findViewById(R.id.removeHabit);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                removeLastHabit();
            }
        }

        );
    }
    private void removeLastHabit(){
        String taskToRemove = morningRituals.get(morningRituals.size()-1);
        ritualState.remove(taskToRemove);
        morningRituals.remove(taskToRemove);

        arrayAdapter.notifyDataSetChanged();
    }
    private void addHabit(){
        EditText editText = inflaterView.findViewById(R.id.editAdd);
        String task = editText.getText().toString();
        morningRituals.add(task);
        ritualState.put(task, false);
        arrayAdapter.notifyDataSetChanged();
    }
    private void loadRitualsFromFile() {
        File root = new File(containerActivity.getExternalFilesDir(null) + "/list");
            try

        {
            File tasksFile = new File(root, "customList");
            FileInputStream inputStream = new FileInputStream(tasksFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            HashMap<String, Boolean> tasks = (HashMap<String, Boolean>) objectInputStream.readObject();
            inputStream.close();
            objectInputStream.close();
            System.out.println(tasks);
            ritualState = new HashMap<String, Boolean>();
            morningRituals = new ArrayList<String>();
            for (String task : tasks.keySet()) {
                morningRituals.add(task);
                ritualState.put(task, tasks.get(task));
            }
            Collections.sort(morningRituals);
        }
            catch(
        Exception e)

        {
            e.printStackTrace();
        }
    }



    // find a way to show it better with things not accomplished? or maybe checked checkboxes

    private void setupListAdapter(View inflater){
        ritualListView = (ListView)inflater.findViewById(R.id.listCustomize);
        Collections.sort(morningRituals);
        arrayAdapter = new ArrayAdapter<String>(containerActivity,
                R.layout.customize_row, R.id.customizeText, morningRituals);

        ritualListView.setAdapter(arrayAdapter);
    }

    private void saveRitualsToFile(HashMap<String, Boolean> tasksList){
        File root = new File(containerActivity.getExternalFilesDir(null) + "/list");
        try{
            File tasksFile = new File(root, "customList");
            FileOutputStream outputStream = new FileOutputStream(tasksFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(tasksList);
            objectOutputStream.close();
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}
