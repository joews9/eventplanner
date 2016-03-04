package com.event.joe.savedevent;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.event.joe.eventfragment.EventDetailsFragment;
import com.event.joe.eventplanner.R;
import com.event.joe.savedevent.MySavedEventsFragment;

import org.json.JSONObject;

public class SavedEventActivity extends Activity implements MySavedEventsFragment.OnEventSaveSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_event);
       SavedEventDetailsFragment msef = new SavedEventDetailsFragment();
        MySavedEventsFragment sef = new MySavedEventsFragment();

        View myView = findViewById(R.id.saved_event_details_container);

        if(myView !=null) {
            msef = new SavedEventDetailsFragment();
            FrameLayout fl = (FrameLayout)myView;
            fl.removeAllViews();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.saved_event_details_container, msef);
            fragmentTransaction.commit();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.saved_event_list_container, sef);
        fragmentTransaction.commit();


    }


    @Override
    public void setEvent(String currentID) {
        SavedEventDetailsFragment eventDetailsFragment =(SavedEventDetailsFragment)getFragmentManager().findFragmentById(R.id.event_details_container);
        eventDetailsFragment.updateInfo(currentID);
    }
}
