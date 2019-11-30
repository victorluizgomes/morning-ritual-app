package com.example.morningritualtracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainPageFragment extends Fragment {

    private Activity containerActivity = null;
    private SimpleAdapter adapter;
    private ListView ritualsView;
    private View inflaterView;
    private List<HashMap<String, String>> rituals;

    private List<String> morningRituals = new ArrayList<>();

    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;



    public MainPageFragment(Activity activity) {
        // Required empty public constructor
        containerActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDefaultRituals();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflaterView = inflater.inflate(R.layout.fragment_main_page_fragement, container, false);
        setupListAdapter(inflaterView);
        setPicButton();
        setCompleteButton();
        setStatButton();
        setHelpButton();
        return inflaterView;
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

    private void setupListAdapter(View inflater) {

        rituals = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < morningRituals.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("task", morningRituals.get(i));
            hm.put("check", "");
            rituals.add(hm);
        }

        String[] from = {"task", "check"};
        int[] to = {R.id.ritualName, R.id.ritualCheck};

        adapter = new SimpleAdapter(containerActivity, rituals,
                R.layout.ritual_row, from, to);
        ritualsView = (ListView) inflater.findViewById(R.id.dailyRitualList);
        ritualsView.setAdapter(adapter);

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(containerActivity.getPackageManager()) != null) {
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
                Uri photoURI = FileProvider.getUriForFile(containerActivity,
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
        File storageDir = containerActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private HashMap<String, Boolean> getChecklistStatus(){
        View v;
        CheckBox box;
        TextView text;
        HashMap<String, Boolean> activitiesList = new HashMap<>();
        for(int i = 0; i < ritualsView.getCount(); i++){
            v = ritualsView.getChildAt(i);
            box = (CheckBox) v.findViewById(R.id.ritualCheck);
            text = (TextView) v.findViewById(R.id.ritualName);
            activitiesList.put((String) text.getText(), box.isChecked());
        }
        return activitiesList;
    }

    public void openStats() {
        Intent intent = new Intent(containerActivity, StatsActivity.class);
        startActivity(intent);
    }
    public void openHelp(){
        HelpFragment fragment = new HelpFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void completeDayPage() {
        saveData(getChecklistStatus(), currentPhotoPath);
        completePageFragment cp = new completePageFragment();
        cp.setContainerActivity(containerActivity);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, cp);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void setPicButton(){
        Button button = (Button) inflaterView.findViewById(R.id.photoBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    public void setCompleteButton(){

        Button button = (Button) inflaterView.findViewById(R.id.completeBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                completeDayPage();
            }
        });
    }

    public void setStatButton(){
        Button button = (Button) inflaterView.findViewById(R.id.statsBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openStats();
            }
        });
    }
    public void setHelpButton(){
        Button button = (Button) inflaterView.findViewById(R.id.helpBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openHelp();
            }
        });
    }
    public void saveData(HashMap<String, Boolean> lists, String path){
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File root = new File(containerActivity.getExternalFilesDir(null) + "/days");
        System.out.println(root.getAbsoluteFile());
        if(!root.exists()){
            root.mkdirs();

        }
        try {
            File saveFile = new File(root, timeStamp);
            FileOutputStream fout = new FileOutputStream(saveFile);
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            CompletedDay complete = new CompletedDay(lists, path);
            oout.writeObject(complete);
            fout.close();
            oout.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
