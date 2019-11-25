package com.example.morningritualtracker;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class StatsActivity extends AppCompatActivity {

    Boolean clicked = true;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        DatePicker simpleDatePicker = (DatePicker)findViewById(R.id.calendar); // initiate a date picker

        simpleDatePicker.setSpinnersShown(false); // set false value for the spinner shown function

        simpleDatePicker.setEnabled(false);

        simpleDatePicker.getCalendarView().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                System.out.println("finally found the listener, the date is: year " + year + ", month "  + ( month + 1) + ", dayOfMonth " + dayOfMonth);
                if (clicked){
                    clicked = false;
                    date = "" + year + (month+1) + dayOfMonth;
                }

            }
        });


//        List<Calendar> dates = new ArrayList<>();
//        dates.add(calendar);
//        Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
//        dpd.setDisabledDays(disabledDays1);
    }

    public static class DownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }


        protected void onPostExecute() {

        }
    }

}
