package com.example.morningritualtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class DayFragment extends Fragment {

    private Activity containerActivity = null;
    private ListView daysView = null;
    private View inflaterView;
    private List<String> thingsDone = new ArrayList<>();

    // TODO: Need to get the picture from the data that day
    // Need to replace the text of the title with the day picked
    // Need to get the list of things accomplished and not accomplished

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflaterView = inflater.inflate(R.layout.fragment_day, container, false);
        setupDefaultsForList();
        setupListAdapter(inflaterView);
        return inflaterView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setupDefaultsForList(){
        thingsDone.add("Brush Teeth");
        thingsDone.add("Take Shower");
        thingsDone.add("Journal");
        thingsDone.add("Meditate");
    }

    // find a way to show it better with things not accomplished? or maybe checked checkboxes

    private void setupListAdapter(View inflater){
        daysView = (ListView)inflater.findViewById(R.id.listCompleted);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(containerActivity,
                R.layout.day_row, R.id.dayCompleted, thingsDone);

        System.out.println(daysView);
        daysView.setAdapter(arrayAdapter);
    }
}
