package com.example.morningritualtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    /*
    This is the main activity of our program but we call the fragments to display our first page.
     */

    MainPageFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = new MainPageFragment(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.outer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStop(){
        super.onStop();
        fragment.onDestroy();
    }


}
