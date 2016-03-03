package com.event.joe.savedevent;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.event.joe.datastorage.MySQLiteHelper;
import com.event.joe.eventplanner.R;

import java.util.HashMap;

public class SavedEventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_event_details);


        String currentEventId = getIntent().getExtras().getString("currentID");

        //recieve the event from the OnListItemClick method
        Bundle bundle = new Bundle();
        bundle.putString("currentID", currentEventId);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SavedEventDetailsFragment edf = new SavedEventDetailsFragment();
        edf.setArguments(bundle);
        fragmentTransaction.add(R.id.saved_event_details_container, edf);
        fragmentTransaction.commit();
        HashMap<String,String> hashMap = new HashMap<>();
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
        hashMap = mySQLiteHelper.getChosenEvent(getIntent().getExtras().getString("currentID"));
        Toast.makeText(this, hashMap.get("title") + hashMap.get("date"), Toast.LENGTH_SHORT).show();

    }
}
