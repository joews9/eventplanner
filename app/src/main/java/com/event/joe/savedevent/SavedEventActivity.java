package com.event.joe.savedevent;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.event.joe.eventplanner.R;
import com.event.joe.savedevent.MySavedEventsFragment;

public class SavedEventActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_event);
        MySavedEventsFragment msef = new MySavedEventsFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.saved_event_list_container, msef);
            fragmentTransaction.commit();


    }
}
