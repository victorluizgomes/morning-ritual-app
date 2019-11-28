package com.example.morningritualtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class completePageFragment extends Fragment {

    // TODO: need to get a motivational quote from an API
    // TODO 2: Get the monthly graph to display instead of the placeholder

    private Activity containerActivity = null;

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_complete_page, container, false);

        return v;
    }
}
