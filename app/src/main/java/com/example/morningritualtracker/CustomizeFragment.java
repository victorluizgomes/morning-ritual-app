package com.example.morningritualtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CustomizeFragment extends Fragment {

    private Activity containerActivity = null;
    private View inflaterView;
    private ListView ritualListView = null;
    private List<String> ritualList = new ArrayList<>();

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflaterView = inflater.inflate(R.layout.fragment_customize, container, false);
        setupDefaultsTestForList();
        setupListAdapter(inflaterView);
        return inflaterView;
    }

    private void setupDefaultsTestForList(){
        ritualList.add("Brush Teeth");
        ritualList.add("Take Shower");
        ritualList.add("Journal");
        ritualList.add("Meditate");
    }

    // find a way to show it better with things not accomplished? or maybe checked checkboxes

    private void setupListAdapter(View inflater){
        ritualListView = (ListView)inflater.findViewById(R.id.listCustomize);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(containerActivity,
                R.layout.customize_row, R.id.customizeText, ritualList);

        ritualListView.setAdapter(arrayAdapter);
    }


}
