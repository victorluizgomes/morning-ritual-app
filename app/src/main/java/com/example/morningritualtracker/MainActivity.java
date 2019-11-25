package com.example.morningritualtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SimpleAdapter adapter;
    private ListView ritualsView;

    private List<HashMap<String, String>> rituals;

    private List<String> morningRituals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadDefaultRituals();
        setupListAdapter();
    }

    private void loadDefaultRituals() {

        morningRituals.add("Meditate");
        morningRituals.add("Drink water");
        morningRituals.add("Go for a 10 minute walk");
        morningRituals.add("Cold shower");
        morningRituals.add("Gratitude journal");
        morningRituals.add("Morning super shake");
    }

    private void setupListAdapter() {

        rituals = new ArrayList<HashMap<String, String>>();

        for(int i = 0; i < morningRituals.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("task", morningRituals.get(i));
            hm.put("check", "");
            rituals.add(hm);
        }

        String[] from = {"task", "check"};
        int[] to = {R.id.ritualName, R.id.ritualCheck};

        adapter = new SimpleAdapter(this, rituals,
                R.layout.ritual_row, from, to);
        ritualsView = (ListView) findViewById(R.id.dailyRitualList);
        ritualsView.setAdapter(adapter);
    }
}
