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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MainPageFragment extends Fragment {

    private Activity containerActivity = null;
    private SimpleAdapter adapter;
    private ListView ritualsView;
    private View inflaterView;
    private List<HashMap<String, Object>> rituals;
    private HashMap<String, Boolean> ritualState;
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
        File root = new File(containerActivity.getExternalFilesDir(null) + "/list");
        if(!root.exists()){
            root.mkdirs();
            try {
                File defaultList = new File(root, "customList");
                HashMap<String, Boolean> tasks = new HashMap<>();
                loadDefaultRituals(tasks);
                FileOutputStream outputStream = new FileOutputStream(defaultList);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(tasks);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    /*
    It will help us to inflate the fragment which we want to display
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflaterView = inflater.inflate(R.layout.fragment_main_page, container, false);
        loadRitualsFromFile();
        setupListAdapter(inflaterView);
        setPicButton();
        setCompleteButton();
        setCustomizeButton();
        setStatButton();
        setHelpButton();
        return inflaterView;
    }

    private void loadRitualsFromFile(){
        File root = new File(containerActivity.getExternalFilesDir(null) + "/list");
        try{
            File tasksFile = new File(root, "customList");
            FileInputStream inputStream = new FileInputStream(tasksFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            HashMap<String, Boolean> tasks = (HashMap<String, Boolean>) objectInputStream.readObject();
            inputStream.close();
            objectInputStream.close();
            System.out.println(tasks);
            ritualState = new HashMap<String, Boolean>();
            morningRituals = new ArrayList<String>();
            for(String task: tasks.keySet()){
                    morningRituals.add(task);
                    ritualState.put(task, tasks.get(task));
            }
            Collections.sort(morningRituals);
        }

        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void saveRitualsToFile(HashMap<String, Boolean> tasksList){
        File root = new File(containerActivity.getExternalFilesDir(null) + "/list");
        try{
            File tasksFile = new File(root, "customList");
            FileOutputStream outputStream = new FileOutputStream(tasksFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(tasksList);
            objectOutputStream.close();
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    // Some default morning rituals
    private void loadDefaultRituals(HashMap<String, Boolean> tasks) {
        tasks.put("Meditate", false);
        tasks.put("Drink water", false);
        tasks.put("Go for a 10 minute walk", false);
        tasks.put("Cold shower", false);
        tasks.put("Gratitude journal", false);
        tasks.put("Morning super shake", false);
    }

    /*
    This will set up the List Adapter for the Rituals
     */
    private void setupListAdapter(View inflater) {
        System.out.println("TEST");
        rituals = new ArrayList<HashMap<String, Object>>();
        Collections.sort(morningRituals);
        for (int i = 0; i < morningRituals.size(); i++) {
            HashMap<String, Object> hm = new HashMap<String, Object>();
            hm.put("task", morningRituals.get(i));
            hm.put("check", ritualState.get(morningRituals.get(i)));
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
    /*
     This will help us to create a image file
     */
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

    /*
    This will create a Map with the Rituals and boolean result.
     */
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
    /*
    If the user clicks the Stats button we will open the stats fragments.
     */
    public void openStats() {
        StatsFragment fragment = new StatsFragment();
        fragment.setContainerActivity(containerActivity);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /*
    This will open the help fragments.
     */
    public void openHelp(){
        HelpFragment fragment = new HelpFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /*
    This will open the customize fragment
     */
    public void openCustomize() {
        CustomizeFragment fragment = new CustomizeFragment();
        fragment.setContainerActivity(containerActivity);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /*
    This will be the fragment details when the user complete his tasks.
     */
    public void completeDayPage() {

        saveCompletedData(getChecklistStatus(), currentPhotoPath);
        System.out.println("COMPLETE");
        CompletePageFragment cp = new CompletePageFragment();

        cp.setContainerActivity(containerActivity);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.outer, cp);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    /*
    This will set the picture in place for the picture placeholder
     */
    public void setPicButton(){
        Button button = (Button) inflaterView.findViewById(R.id.photoBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }
    /*
    When the button is clicked it goes to open the complete fragments.
     */
    public void setCompleteButton(){

        Button button = (Button) inflaterView.findViewById(R.id.completeBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                completeDayPage();
            }
        });
    }
    /*
        When the button is clicked it goes to open the stats fragments.
    */
    public void setStatButton(){
        Button button = (Button) inflaterView.findViewById(R.id.statsBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openStats();
            }
        });
    }

    /*
    When the button is clicked it goes to open the customize fragments.
     */

    public void setCustomizeButton() {
        Button button = (Button) inflaterView.findViewById(R.id.customizeBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openCustomize();
            }
        });
    }

    /*
    When the button is clicked it goes to open the help fragments.
     */
    public void setHelpButton(){
        Button button = (Button) inflaterView.findViewById(R.id.helpBtn);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openHelp();
            }
        });
    }


    /*
    When the button is clicked it will save the user data..
     */

    public void saveCompletedData(HashMap<String, Boolean> lists, String path){
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
    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("DESTROY");
        HashMap<String, Boolean> checkReset = new HashMap<>();
        for(String task: ritualState.keySet()){
            checkReset.put(task, false);
        }
        saveRitualsToFile(checkReset);
    }
    @Override
    public void onPause(){
        super.onPause();
        saveRitualsToFile(getChecklistStatus());
    }
}
