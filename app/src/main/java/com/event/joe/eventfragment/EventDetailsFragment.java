package com.event.joe.eventfragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.event.joe.datastorage.MySQLiteHelper;
import com.event.joe.eventplanner.MapsActivity;
import com.event.joe.eventplanner.R;
import com.event.joe.servicepackage.AbstractService;
import com.event.joe.servicepackage.EventSearchService;
import com.event.joe.servicepackage.ImageDownloadService;
import com.event.joe.servicepackage.ServiceListener;
import com.event.joe.servicepackage.WeatherSearchService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Joe Aimee and Baby on 09/02/2016.
 */
public class EventDetailsFragment extends Fragment implements ServiceListener {


    private static String eventDate;
    private static String temperature;
    private static String weatherType;
    private static String weatherDate;
    private String location;
    private String title;
    private int eventInt;
    private JSONObject event;
    private String date;
    private String currentEventString;
    private String stringLongitude;
    private String stringLatitude;
    private String postcode;
    private String ticketURL;

    //All the Views
    private TextView weatherTypeView;
    private TextView temperatureView;
    private TextView tvDate;
    private TextView tvLocation;
    private TextView tvDescription;
    private TextView tvTitle;
    private ImageView weatherimg;
    private ImageView imgEvent;


    MySQLiteHelper sqLiteHelper;

    private Thread imageThread;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            rootView = inflater.inflate(R.layout.event_details_layout_landscape, container, false);
        }else{
            rootView = inflater.inflate(R.layout.event_details_layout, container, false);
        }

        //set up the buttons
        Button btnMapsLocation;
        Button btnDirections;
        Button btnTickets;
        Button btnShare;
        Button btnSave;


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ){
            System.out.println(currentEventString);
            try{
                currentEventString = getArguments().getString("currentEvent");
                event  = new JSONObject(currentEventString);
            }catch(Exception e){}
            displayEventDetails();
        }

        if (container != null) {
            container.removeAllViews();
        }

        //Button for maps
        btnMapsLocation = (Button)rootView.findViewById(R.id.btnLocation);
        btnMapsLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("BUTTON CLICK");
                Intent mapsIntent = new Intent(getActivity(), MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("lat", stringLatitude);
                bundle.putString("long", stringLongitude);
                bundle.putString("eventTitle", title);
                mapsIntent.putExtras(bundle);
                try {
                    startActivity(mapsIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Not Available", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Save Button
        btnSave = (Button)rootView.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("****************** Save Event");
                sqLiteHelper = new MySQLiteHelper(getActivity());
                sqLiteHelper.saveEvent(event);
                Toast.makeText(getActivity(), "Event Saved!", Toast.LENGTH_LONG).show();
            }
        });

        //Button directions
        btnDirections = (Button)rootView.findViewById(R.id.btnDirections);
        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+postcode);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        //Button share
        btnShare = (Button)rootView.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareText = "I'm interested in this event: \n" + title + "\non : " + date + "\nCheck the link: " + ticketURL;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
               startActivity(intent);

            }
        });

        imgEvent = (ImageView)rootView.findViewById(R.id.imgEvent);

        //Button for tickets
        btnTickets = (Button)rootView.findViewById(R.id.btnTickets);
        btnTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(ticketURL));
                startActivity(i);
            }
        });

        return rootView;
    }

    /**
     *
     * This will update the information from the  listener
     * @param event
     */
    public void updateInfo(JSONObject event){
        this.event = event;
        displayEventDetails();
    }

    /**
     * This method uses all the necessary fields to set the details of the event
     */
    public void displayEventDetails(){

        tvDate = (TextView)rootView.findViewById(R.id.tvDate);
        tvTitle = (TextView)rootView.findViewById(R.id.tvTitle);
        tvLocation = (TextView)rootView.findViewById(R.id.tvLocation);


        try{
            stringLatitude = event.getString("latitude");
            stringLongitude = event.getString("longitude");
            date = event.getString("start_time");
            location = event.getString("venue_name");
            title = event.getString("title");
            tvLocation.setText("location: " + location);
            tvTitle.setText(title);
            tvDate.setText("Date: " + date);
            eventDate = date.substring(8, 10);
            eventInt = Integer.parseInt(eventDate);
            ticketURL = event.getString("url");
            postcode = event.getString("postal_code");

            String imagePath=
                    getActivity().getDir("MyFilmLib", Context.MODE_PRIVATE).getAbsolutePath() +
                            "/MyFilmLib" + postcode + ".png";
            File f = new File(imagePath);

                ImageDownloadService downloadService = new ImageDownloadService(
                       event.getJSONObject("image").getJSONObject("medium").getString("url"), imagePath);
                downloadService.addListener(this);
                imageThread = new Thread(downloadService);
                imageThread.start();
            doSearch(location);


        }catch(Exception e){

        }

    }

    @Override
    public void ServiceComplete(AbstractService service) {
        if (!service.hasError()) {
            if(service instanceof EventSearchService || service instanceof WeatherSearchService) {
                final WeatherSearchService weatherService = (WeatherSearchService) service;
                JSONArray currentWeather = new JSONArray();
                currentWeather = weatherService.getResults();
                System.out.println(currentWeather);
                try {

                    //Get the weather information
                    boolean isMatched = true;
                    String dateTemp;
                    JSONObject weatherCurrent = new JSONObject();

                    //iterate through all weather results until the day matches with the event day
                    for (int i = 0; i < 39; i++) {

                        //get the date and take out the day number and convert to int
                        dateTemp = currentWeather.getJSONObject(i).getString(
                                "dt_txt");
                        System.out.println(dateTemp);
                        weatherDate = dateTemp.substring(8, 10);
                        System.out.println(weatherDate);
                        int weatherInt = Integer.parseInt(weatherDate);
                        System.out.println(weatherInt + "" + eventInt);

                        if (weatherInt == eventInt) {
                            System.out.println(weatherInt);
                            System.out.println(eventInt);
                            weatherCurrent = currentWeather.getJSONObject(i);
                            weatherType = currentWeather.getJSONObject(i)
                                    .getJSONArray("weather").getJSONObject(0)
                                    .getString("main");
                            System.out.println("^^^^^^^^^" + weatherType);
                            temperature = currentWeather.getJSONObject(i)
                                    .getJSONObject("main").getString("temp");

                            weatherTypeView = (TextView) rootView.findViewById(R.id.tvWeatherType);
                            weatherTypeView.setText(weatherType);
                            weatherimg = (ImageView) rootView.findViewById(R.id.imgWeather);

                            //set the weather type image e.g sun, clouds etc
                            setWeatherTypeImage(weatherimg, weatherType);
                            temperatureView = (TextView) getView().findViewById(R.id.tvTemp);

                            //take the temperature and convert from kelvin to celcius
                            String tempCut = temperature.substring(0, 3);
                            double celciusTemp = Integer.parseInt(tempCut);
                            celciusTemp = Math.round(celciusTemp - 273.15);
                            temperatureView.setText(celciusTemp + " °C");


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(service instanceof ImageDownloadService){
                File f = new File(((ImageDownloadService)service).getLocal());
                if(f.exists()){
                    imgEvent.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
                }
            }

        } else {

        }
    }

    private void setWeatherTypeImage(ImageView weatherimg, String weatherType) {
        this.weatherType = weatherType;

        switch(weatherType){
            case "Clouds":
                weatherimg.setImageResource(R.mipmap.cloud);
                break;
            case "Clear":
                weatherimg.setImageResource(R.mipmap.sun);
                break;
            case "Snow":
                weatherimg.setImageResource(R.mipmap.snow);
                break;
            case "Rain":
                weatherimg.setImageResource(R.mipmap.rain);
                break;
        }
    }

    private void doSearch(String town) {
        // TODO Auto-generated method stub
        String[] result = new String[] { "Searching..." };
        // create event search service object which will query the web services
        // using the search criteria
        WeatherSearchService service = new WeatherSearchService(town);
        service.addListener(this);
        Thread thread = new Thread(service);
        thread.start();

    }

    /**
     * This method will set the weather text into the views
     */
    private void weatherTextView() {
        weatherTypeView = (TextView)getView().findViewById(R.id.tvWeatherType);
        weatherTypeView.setText(weatherType);
        temperatureView = (TextView)getView().findViewById(R.id.tvTemp);
        temperatureView.setText(temperature);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


}