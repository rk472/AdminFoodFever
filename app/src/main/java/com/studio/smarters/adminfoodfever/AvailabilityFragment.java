package com.studio.smarters.adminfoodfever;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AvailabilityFragment extends Fragment {
    private AppCompatActivity main;
    private View root;

    public AvailabilityFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        main=(AppCompatActivity)getActivity();
        main.getSupportActionBar().setTitle("Change Availability");
        root=inflater.inflate(R.layout.fragment_availability, container, false);
        //Nav View
        NavigationView navigationView = (NavigationView) main.findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_change_availability);


        // Inflate the layout for this fragment
        return root;
    }

}
