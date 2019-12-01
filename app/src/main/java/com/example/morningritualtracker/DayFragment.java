package com.example.morningritualtracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
        Bundle args = getArguments();
        CompletedDay day = (CompletedDay) args.get("day");
        String date = args.getString("date");

        if(day != null) {
            setupTaskList(day.getListViewItems());
            setupImageView(day.getImagePath());
        }

        setupListAdapter(inflaterView);
        setupTitleDate(date);
        return inflaterView;
    }

    private void setupTitleDate(String date) {
        Calendar cal = Calendar.getInstance();
        try {
            Date realDate = new SimpleDateFormat("yyyyMMdd").parse(date);
            cal.setTime(realDate);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String month = Integer.toString(cal.get(Calendar.MONTH));
        String year  = Integer.toString(cal.get(Calendar.YEAR));
        String day   = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));

        String title = month + "/" + day + "/" + year;
        TextView tv = (TextView) inflaterView.findViewById(R.id.dateTitle);
        tv.setText(title);

    }

    private void setupImageView(String imagePath) {

        ImageView iv = inflaterView.findViewById(R.id.stockPhoto);

        File imgFile = new File(imagePath);
        Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, 200, 350, true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        iv.setImageBitmap(rotatedBitmap);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setupTaskList(HashMap<String, Boolean> tasks){

        for (String task : tasks.keySet()) {
            if (tasks.get(task)) {
                thingsDone.add(task);
            }
        }

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
