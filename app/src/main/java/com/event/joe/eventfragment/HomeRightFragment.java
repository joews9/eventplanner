package com.event.joe.eventfragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.event.joe.eventplanner.EventActivity;
import com.event.joe.eventplanner.R;
import com.event.joe.savedevent.SavedEventActivity;

/**
 * Created by Joe Aimee and Baby on 01/03/2016.
 */
public class HomeRightFragment extends Fragment {

    private String location;
    private String distance;
    private String eventTimescale;
    private String eventCategory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
           view = inflater.inflate(R.layout.home_main_layout_landscape, container, false);
        }else{
            view = inflater.inflate(R.layout.home_main_layout, container, false);
        }


        Button button = (Button)view.findViewById(R.id.btnGo);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
         //send the location and distance to EventActivity
                EditText nameTown = (EditText)getView().findViewById(R.id.enteredTown);
                location = nameTown.getText().toString();
                EditText etDistance = (EditText)getView().findViewById(R.id.etDistance);
                distance = etDistance.getText().toString();
                Spinner spinnerWhen = (Spinner)getView().findViewById(R.id.spinnerWhen);
                eventTimescale =  spinnerWhen.getSelectedItem().toString();
                Spinner spinnerCat = (Spinner)getView().findViewById(R.id.spinnerCat);
                eventCategory = spinnerCat.getSelectedItem().toString();
                Intent intent = new Intent(getActivity(), EventActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("location", location);
                bundle.putString("distance", distance);
                bundle.putString("eventTimescale", eventTimescale);
                bundle.putString("eventCategory",  eventCategory);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button btnSavedEvents = (Button)view.findViewById(R.id.btnMyEvents);
        btnSavedEvents.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SavedEventActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
