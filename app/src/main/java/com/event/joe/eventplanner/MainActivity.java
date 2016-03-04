package com.event.joe.eventplanner;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.event.joe.eventfragment.HomeLeftFragment;
import com.event.joe.eventfragment.HomeRightFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeLeftFragment hlf;
        HomeRightFragment hrf = new HomeRightFragment();

        View myView = findViewById(R.id.fragment_left_container);

        if(myView !=null) {
            hlf = new HomeLeftFragment();
            FrameLayout fl = (FrameLayout)myView;
            fl.removeAllViews();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_left_container, hlf);
            fragmentTransaction.commit();
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_right_container, hrf);
        fragmentTransaction.commit();


    }
}
