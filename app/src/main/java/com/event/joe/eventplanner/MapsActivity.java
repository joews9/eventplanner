package com.event.joe.eventplanner;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String stringLatitude;
    private String stringLongitude;
    private String eventPreCut;
    private String event;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        stringLatitude = getIntent().getExtras().getString("lat");
        stringLongitude = getIntent().getExtras().getString("long");
        eventPreCut = getIntent().getExtras().getString("eventTitle");

        latitude = Double.parseDouble(stringLatitude);
        longitude = Double.parseDouble(stringLongitude);
        event = eventPreCut;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker to event location
        LatLng eventLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(eventLocation).title(event));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 16));
}

}