package com.example.morningritualtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class StatsFragment extends Fragment {

    private Activity containerActivity = null;
    private View inflaterView;

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflaterView = inflater.inflate(R.layout.fragment_stats, container, false);
        setSeeDayButton();
        return inflaterView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatePicker simpleDatePicker = (DatePicker)containerActivity.findViewById(R.id.calendar);

        // TODO: this takes the text but breaks the UI
        // TODO 2: have the Calendar take less space, maybe a different type
        /*simpleDatePicker.getCalendarView().setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                System.out.println("finally found the listener, the date is: year " + year + ", month "  + ( month + 1) + ", dayOfMonth " + dayOfMonth);
                if (clicked){
                    clicked = false;
                    date = "" + year + (month+1) + dayOfMonth;
                }
            }
        });*/

        /*List<Calendar> dates = new ArrayList<>();
        dates.add(calendar);
        Calendar[] disabledDays1 = dates.toArray(new Calendar[dates.size()]);
        dpd.setDisabledDays(disabledDays1); */
    }

    public void openDay(){
        DayFragment fragment = new DayFragment();
        fragment.setContainerActivity(containerActivity);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setSeeDayButton() {
        Button button = (Button) inflaterView.findViewById(R.id.seeDay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDay();
            }
        });
    }
}
