package com.event.joe.eventfragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.event.joe.eventplanner.R;

/**
 * Created by Joe Aimee and Baby on 01/03/2016.
 */
public class HomeLeftFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);

        return view;
    }
}
