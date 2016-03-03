package com.event.joe.eventfragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.event.joe.com.event.joe.eventlistadapter.EventListAdapter;
import com.event.joe.com.event.joe.eventlistadapter.EventListDetailProvider;
import com.event.joe.eventplanner.EventDetailsActivity;
import com.event.joe.eventplanner.R;
import com.event.joe.servicepackage.AbstractService;
import com.event.joe.servicepackage.EventSearchService;
import com.event.joe.servicepackage.ServiceListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Joseph Millership
 */
public class EventViewFragment extends Fragment implements ServiceListener{
    private  ArrayAdapter<String> adapter;
    private View myView;
    private ListView lv;
    private ArrayList<JSONObject> searchResults;
    private TextView number;
    String[] resultTitle;
    String[] resultDate;
    OnEventSetListener onEventSetListener;
    EventListAdapter eventAdapter;
    private JSONArray events;
    private String location;
    private String distance;
    private String eventTimescale;
    private String eventCategory;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.events_view_layout, container, false);
        //check to see if there is any fragments already in the view


        if (container != null) {
            container.removeAllViews();
        }

        lv = (ListView)view.findViewById(R.id.listGo);
        searchResults = new ArrayList<JSONObject>();

        //Retrieve the values from the activity


        return view;
    }

    private void doSearch(String town, String distance, String eventCategory, String eventTimescale) {
        // TODO Auto-generated method stub
        resultTitle = new String[] { "Searching" };
        resultDate = new String[]{"..."};

        //Search for the events
        EventSearchService service = new EventSearchService(town, distance, eventCategory, eventTimescale);
        service.addListener(this);
        Thread thread = new Thread(service);
        thread.start();
        setList();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            location = getArguments().getString("location");
            distance = getArguments().getString("distance");
            eventCategory = getArguments().getString("eventCategory");
            eventTimescale = getArguments().getString("eventTimescale");
            doSearch(location, distance, eventCategory, eventTimescale);
            Toast.makeText(getActivity(), eventTimescale + eventCategory, Toast.LENGTH_LONG).show();


    }

    @Override
    public void ServiceComplete(AbstractService service) {
        if (!service.hasError()) {
            // convert the abstractService object passed into the method to a
            // EventSearchService object
            final EventSearchService eventService = (EventSearchService) service;
            // create a string array that is the same size as the results
            // JSONArray
            events = eventService.getResults();
            resultTitle = new String[events.length()];
            resultDate = new String[events.length()];
            // clear the arraylist
            searchResults.clear();
            setAllDetails();
        } else {
            //if there are no results display no results string in the list
            String[] resultTitle = new String[] { "No" };
            String[] resultDate = new String[]{"Results"};
            setList();
        }

    }

    public void setAllDetails(){
        // loop through the JSONArray and get the title of each JSONObject
        for (int i = 0; i < events.length(); i++) {
            try {
                searchResults.add(events
                        .getJSONObject(i));
                resultTitle[i] = events.getJSONObject(i)
                        .getString("title");
                resultDate[i] = events.getJSONObject(i).getString("start_time");
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {

                        //if the device is in portrait
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ) {
                            JSONObject currentEvent = null;
                            try {
                                currentEvent = events.getJSONObject(position);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(getActivity(),
                                    EventDetailsActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("currentEvent", currentEvent.toString());
                            intent.putExtras(extras);
                            startActivity(intent);
                        }
                        //if the device is in landscape
                        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                            JSONObject currentEvent = null;
                            try{
                                currentEvent = events.getJSONObject(position);
                            }catch(JSONException e){}

                            onEventSetListener.setEvent(currentEvent);
                        }

                    }
                });

            } catch (JSONException ex) {
                resultTitle[i] = "Error";
            }
        }

        //Setting the list Adapter*****************************************************************************//
        setList();
    }

    public interface OnEventSetListener{

        public void setEvent(JSONObject event);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{

            onEventSetListener = (OnEventSetListener)activity;

        }catch(Exception ex){}

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("events", events.toString());

    }


    public void setList(){
        int i = 0;
       EventListAdapter eventAdapter = new EventListAdapter(getActivity(),R.layout.event_list_cell);
        lv.setAdapter(eventAdapter);

        for(String event_titles: resultTitle) {
            EventListDetailProvider dataProvider = new EventListDetailProvider(resultTitle[i], resultDate[i]);
            eventAdapter.add(dataProvider);
            i++;
        }
    }
}
