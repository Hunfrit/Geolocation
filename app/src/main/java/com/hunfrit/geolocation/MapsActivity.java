package com.hunfrit.geolocation;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String first = (String) getIntent().getSerializableExtra("lit");
        String second = (String) getIntent().getSerializableExtra("lon");

        // Add a marker in Kropyvnytskyi and move the camera

        Geocoder geocoder;

        geocoder = new Geocoder(this, Locale.getDefault());
        String currentAddress = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.valueOf(first), Double.valueOf(second), 1);
            if(addresses != null){
                currentAddress = addresses.get(0).getAddressLine(0);
            }else{
                currentAddress = "No Address returned!";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        LatLng kropyvnytskyi = new LatLng(Double.valueOf(first), Double.valueOf(second));
        mMap.addMarker(new MarkerOptions().position(kropyvnytskyi).title(currentAddress));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(kropyvnytskyi));
    }
}
