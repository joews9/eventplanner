package com.event.joe.eventplanner;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.event.joe.eventfragment.EventDetailsFragment;

import org.json.JSONObject;

public class EventDetailsActivity extends AppCompatActivity {

    private JSONObject event;
    private String eventString;
    private JSONObject currentEvent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        if(savedInstanceState == null){
            try{
                currentEvent = new JSONObject(getIntent().getExtras()
                        .getString("currentEvent"));
            }catch(Exception e){e.printStackTrace();}
        }else if(savedInstanceState !=null){
            eventString = savedInstanceState.getString("event");
            try{
                currentEvent = new JSONObject(eventString);
            }catch(Exception ex){ex.printStackTrace();}

        }

        //recieve the event from the OnListItemClick method
        bundle = new Bundle();
        bundle.putString("currentEvent" , currentEvent.toString());
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        EventDetailsFragment edf = new EventDetailsFragment();
        edf.setArguments(bundle);
        fragmentTransaction.add(R.id.event_details_container, edf);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
