package com.example.morningritualtracker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

public class CompletePageFragment extends Fragment {

    // TODO 2: Get the monthly graph to display instead of the placeholder

    //Uses the paper quotes api to grab quotes
    String apiKey = "f996a4ca824287b5197b5c0057aabeee8d3fff16";
    View inflaterView;

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
}
