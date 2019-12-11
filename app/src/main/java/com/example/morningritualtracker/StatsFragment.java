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

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


public class StatsFragment extends Fragment {

    private Activity containerActivity = null;
    private View inflaterView;
    private TreeMap<Date, CompletedDay> graphData;
    private String start_date;

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY = new ArrayList<BarEntry>();
    ArrayList<String> BarEntryLabels = new ArrayList<String>();
    BarDataSet Bardataset ;
    BarData BARDATA ;

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflaterView = inflater.inflate(R.layout.fragment_stats, container, false);
        setSeeDayButton();
        chart = (BarChart) inflaterView.findViewById(R.id.graph);
        display_graph();
        return inflaterView;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatePicker simpleDatePicker = (DatePicker)containerActivity.findViewById(R.id.calendar);
        loadGraphData();
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

        //Grabs date from calendar to yyyyMMdd format String
        DatePicker selectDate = (DatePicker) inflaterView.findViewById(R.id.calendar);

        String year = Integer.toString(selectDate.getYear());
        String month = Integer.toString(selectDate.getMonth()+1);
        String day = Integer.toString(selectDate.getDayOfMonth());
        if(month.length() == 1){
            month = "0" + month;
        }
        if(day.length() == 1){
            day = "0" + day;
        }

        String dateSelected = year+month+day;
        //Passes data on to new fragment
        Bundle args = new Bundle();
        args.putSerializable("day", getDate(dateSelected));
        args.putString("date", dateSelected);


        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
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

    /*
        This will tell us if there is an entry for a particular date or not.
     */

    public CompletedDay getDate(String dateSelected){
        CompletedDay retrievedDate = null;
        try {
             retrievedDate = graphData.get(new SimpleDateFormat("yyyyMMdd").parse(dateSelected));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return retrievedDate;
    }
    /*
    This will helps us to display our graph on the screen.
     */
    public void display_graph() {

        BARENTRY = new ArrayList<>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "WEEKlY TASK");

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(BARDATA);

        chart.animateY(3000);

    }
    /*
    In this function we are potting our points.
     */
    public void AddValuesToBARENTRY(){
        HashMap<Integer,Integer> work = group_by_month();
        for(int i=1;i<=5;i++) {
            Integer key = i;
            Integer value = work.get(i);
            System.out.println(value + "----"+key);
            BARENTRY.add(new BarEntry(value,key));
        }

    }
    /*
    Adds the labels for the Bar graph
    */
    public void AddValuesToBarEntryLabels(){
        BarEntryLabels.add("WEEk1");
        BarEntryLabels.add("WEEk2");
        BarEntryLabels.add("WEEk3");
        BarEntryLabels.add("WEEk4");
        BarEntryLabels.add("WEEk5");
    }
    /*
    This method is used to build our MAP for Weeklyt activity.
     */
    public HashMap<Integer,Integer> group_by_month()  {
        HashMap<Integer,Integer> work = new HashMap<>();
        CompletedDay retrievedDate;
        List<String> dates = find_dates();
        int j = 1;
        int total_things = 0;
        for(int i=0;i<dates.size();i++) {
            if (i % 7 == 0 && i != 0) {
                work.put(j,total_things);
                j += 1;
                total_things = 0;
            }

            try {
                System.out.println(dates.get(i));
                retrievedDate = getDate(dates.get(i));
                for (Map.Entry mapElement : retrievedDate.listViewItems.entrySet()) {
                    String key = (String) mapElement.getKey();
                    boolean value = (boolean) mapElement.getValue();
                    if (value){
                        total_things += 1;
                    }
                    System.out.println(key + " : " + value);
                    System.out.println(" Date is ..." + dates.get(i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        work.put(j,total_things);
        return work;
    }
    /*
    This will helps us to build a list of dates for a particular month.
     */
    public List<String> find_dates(){
        List<String> dates = new ArrayList<String>();
        int current_month =  Calendar.getInstance().get(Calendar.MONTH) + 1;
        int current_year  = Calendar.getInstance().get(Calendar.YEAR) ;

        int end_s  = 30;
        if (current_month == 1 || current_month == 3 || current_month == 5 || current_month == 7
                || current_month == 8 || current_month == 10 || current_month == 12){
            end_s = 31;
        }
        if (current_month == 2){
            if (check_leap(current_year)){
                end_s = 29;
            }
            else{
                end_s = 28;
            }
        }
        int start = 1;
        while (start <= end_s) {
            start_date = current_year + "" + current_month + "" + start;
            dates.add(start_date);
            start += 1;
        }
        return dates;
    }
    /*
    check_leap(): it will helps us to identify if a year is a leap year or not.
     */
    public  boolean check_leap(int year){
        boolean isLeap = false;
        if(year % 4 == 0)
        {
            if( year % 100 == 0)
            {
                if ( year % 400 == 0)
                    isLeap = true;
                else
                    isLeap = false;
            }
            else
                isLeap = true;
        }
        else {
            isLeap = false;
        }
        return  isLeap;
    }
    /*
        loadGraphData(): This is will help us to load our data from the file.
        All the data is stored in a TreeMap. We have used treeMap as to automatically sort the data
        according to the dates.
     */
    public void loadGraphData(){
        File root = new File(containerActivity.getExternalFilesDir(null) + "/days");
        graphData = new TreeMap<Date, CompletedDay>();
        FileInputStream fileIn;
        ObjectInputStream objectIn;
        try {
            for (File file : root.listFiles()) {
                fileIn = new FileInputStream(file);
                objectIn = new ObjectInputStream(fileIn);
                CompletedDay day = (CompletedDay) objectIn.readObject();
                Date date = new SimpleDateFormat("yyyyMMdd").parse(file.getName());
                graphData.put(date, day);
            }
        }
        catch(Exception e){e.printStackTrace();}
    }
}
