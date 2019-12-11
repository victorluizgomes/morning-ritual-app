package com.example.morningritualtracker;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.jinatonic.confetti.CommonConfetti;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class CompletePageFragment extends Fragment {

    private TreeMap<Date, CompletedDay> graphData;

    //Uses the paper quotes api to grab quotes
    String apiKey = "f996a4ca824287b5197b5c0057aabeee8d3fff16";
    View inflaterView;
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY = new ArrayList<>();
    ArrayList<String> BarEntryLabels = new ArrayList<>();
    BarDataSet Bardataset ;
    BarData BARDATA ;

    private Activity containerActivity = null;

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        inflaterView = inflater.inflate(R.layout.fragment_complete_page, container, false);
        new QuoteDownloader().execute();

        chart = (BarChart) inflaterView.findViewById(R.id.completeGraph);
        loadGraphData();
        display_graph();
        CommonConfetti.rainingConfetti(container, new int[]{Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE}).oneShot();
        return inflaterView;
    }

    private class QuoteDownloader extends AsyncTask<URL, Integer, String>{

        @Override
        protected String doInBackground(URL... urls) {
            try {
                URL url = new URL("https://api.paperquotes.com/apiv1/quotes/?&curated=1&limit=4");
                URLConnection urlConn = url.openConnection();
                urlConn.setRequestProperty("Authorization", "Token " + apiKey);

                BufferedReader content = new BufferedReader(new InputStreamReader((InputStream) urlConn.getContent()));
                JSONObject json = createJson(content);
                Random rand = new Random();
                int index = rand.nextInt(4);
                String quote = json.getJSONArray("results").getJSONObject(index).getString("quote");
                String author = json.getJSONArray("results").getJSONObject(index).getString("author");
                if(author.length() < 2){
                    author = "Unknown";
                }

                return quote + "\n\n - " + author;

            }
            catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String quote){
            TextView tv = (TextView) inflaterView.findViewById(R.id.motivationQuote);
            tv.setText(quote);
        }
    }

    /*
    This will helps us to get the quote for us.
     */
    private JSONObject createJson(BufferedReader in){
        String json = "";
        String line;
        try {
            while ((line = in.readLine()) != null) {
                json += line;
            }
        in.close();
            System.out.println(json);
        return new JSONObject(json);
        }
        catch (Exception e){

        }
        return null;
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
        Bardataset = new BarDataSet(BARENTRY, "MONTHLY TASK");

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
        for (int m  = 1 ; m <= 12 ; m++){
            int key = m;
            int value = work.get(m);
            System.out.println(value + "---" + key);
            BARENTRY.add(new BarEntry(value,key));
        }
    }
    /*
    Adds the labels for the Bar graph
     */
    public void AddValuesToBarEntryLabels(){
        BarEntryLabels.add("JAN");
        BarEntryLabels.add("FEB");
        BarEntryLabels.add("MAR");
        BarEntryLabels.add("APR");
        BarEntryLabels.add("MAY");
        BarEntryLabels.add("JUNE");
        BarEntryLabels.add("JULY");
        BarEntryLabels.add("AUG");
        BarEntryLabels.add("SEP");
        BarEntryLabels.add("OCT");
        BarEntryLabels.add("NOV");
        BarEntryLabels.add("DEC");
    }
    /*
    group_by_month() : This  aims to group all the activity done by month in a hashmap which we could
    display in the bar chart.
     */
    public HashMap<Integer,Integer> group_by_month()  {
        HashMap<Integer,Integer> work = new HashMap<>();
        CompletedDay retrievedDate;
        HashMap<Integer,List<String>> groupMonth = find_dates();

        for (int m  = 1 ; m <= 12 ; m++){
            Integer month      = m;
            List<String> dates = (List<String>) groupMonth.get(m);
            int total_things = 0;
            for(int i = 0; i < dates.size() ; i++){
                retrievedDate = getDate(dates.get(i));
                if (retrievedDate  == null){
                    total_things += 0;
                }else{
                    for (Map.Entry mapElement : retrievedDate.listViewItems.entrySet()) {
                        boolean value = (boolean) mapElement.getValue();
                        if (value){
                            total_things += 1;
                        }
                    }
                }

            }
            // Put all the months activity by the month number in the Map.
            work.put( month, total_things );
        }
        return work;
    }

    public HashMap<Integer,List<String>> find_dates(){
        // It will group each month by a integer and attach a list of strings that is the dates.
        HashMap<Integer,List<String>> groupMonth =  new HashMap<>();
        // Grouping all the months in a list.
        List<String>month_number = new ArrayList<>();
        month_number.add("01");
        month_number.add("02");
        month_number.add("03");
        month_number.add("04");
        month_number.add("05");
        month_number.add("06");
        month_number.add("07");
        month_number.add("08");
        month_number.add("09");
        month_number.add("10");
        month_number.add("11");
        month_number.add("12");
        // get the current year.
        int current_year  = Calendar.getInstance().get(Calendar.YEAR) ;
        for(int i = 0 ; i < month_number.size() ; i++){
            List<String> dates = new ArrayList<String>();
            int end = check_month(i,current_year);
            int start = 1;
            // List all the  dates in a list.
            while (start <= end) {
                String start_date = current_year + month_number.get(i)+ start;
                dates.add(start_date);
                start += 1;
            }
            groupMonth.put(i+1, dates);
        }
        return groupMonth;
    }
    /*
    check_month(): It will help us to set our date end point. This method will return the last date
    in a month according to the year.
     */
    public int check_month(int current_month , int current_year){
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
        return end_s;
    }

    /*
    check_leap(): it will helps us to identify if a year is a leap year or not.
     */
    public  boolean check_leap(int year){
        boolean isLeap = false;
        if(year % 4 == 0)
        { if( year % 100 == 0){
            if ( year % 400 == 0)
                isLeap = true;
            else
                isLeap = false;
        } else
            isLeap = true;
        } else {
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
