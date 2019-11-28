package com.example.morningritualtracker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    // TODO: Main page needs to either be a fragment, or other pages need to be activities

    private SimpleAdapter adapter;
    private ListView ritualsView;

    private List<HashMap<String, String>> rituals;

    private List<String> morningRituals = new ArrayList<>();

    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public void takePic(View v){
        dispatchTakePictureIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadDefaultRituals();
        setupListAdapter();
    }

    // Some default morning rituals
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

        for (int i = 0; i < morningRituals.size(); i++) {
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            System.out.println(currentPhotoPath);
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.morningritualtracker.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void openStats(View v) {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    public void completeDayPage(View v) {
        completePageFragment cp = new completePageFragment();
        cp.setContainerActivity(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, cp);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
