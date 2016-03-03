package com.event.joe.eventplanner;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.event.joe.eventfragment.EventDetailsFragment;
import com.event.joe.eventfragment.EventViewFragment;
import com.google.android.gms.vision.Frame;

import org.json.JSONObject;

public class EventActivity extends AppCompatActivity implements EventViewFragment.OnEventSetListener {
    private String location;
    private String distance;
    private String eventTimescale;
    private String eventCategory;
    EventViewFragment evf = new EventViewFragment();
    EventDetailsFragment edf = new EventDetailsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);



        //get the intent from SplashActivity
        System.out.println("*******************2 BROKEN");
        Intent intent = getIntent();
        if(intent !=null){
            System.out.println("*******************NO Intent EventActivity");
        }
        distance = getIntent().getExtras().getString("distance");
        location = getIntent().getExtras().getString("location");
        eventTimescale = getIntent().getExtras().getString("eventTimescale");
        eventCategory = getIntent().getExtras().getString("eventCategory");

        //make a bundle to send to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("location", location);
        bundle.putString("distance", distance);
        bundle.putString("eventCategory", eventCategory);
        bundle.putString("eventTimescale" , eventTimescale);

        View myView = findViewById(R.id.event_details_container);

        if(myView !=null) {
            edf = new EventDetailsFragment();
            FrameLayout fl = (FrameLayout)myView;
            fl.removeAllViews();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.event_details_container, edf);
            evf.setArguments(bundle);
            fragmentTransaction.commit();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        evf.setArguments(bundle);
        fragmentTransaction.add(R.id.event_list_container, evf);
        fragmentTransaction.commit();

    }

    @Override
    public void setEvent(JSONObject event) {
        EventDetailsFragment eventDetailsFragment =(EventDetailsFragment)getFragmentManager().findFragmentById(R.id.event_details_container);
        eventDetailsFragment.updateInfo(event);
    }


}
