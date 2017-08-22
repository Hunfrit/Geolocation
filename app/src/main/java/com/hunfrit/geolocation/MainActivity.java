package com.hunfrit.geolocation;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sPref, sharedPreferences;
    TextView startPoint, currentLocation, distanceTV;
    String LIT, LON;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        startPoint = (TextView) findViewById(R.id.startPoint);
        currentLocation = (TextView) findViewById(R.id.currentLocation);
        distanceTV = (TextView) findViewById(R.id.distance);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("TAGA", "WTF");

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("lit", LIT);
                intent.putExtra("lon", LON);
                startActivity(intent);
            }
        });
    }

    public void startTracking (View view){
        String first = LIT;
        String second = LON;
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("LIT", first);
        ed.putString("LON", second);
        ed.commit();
        startPoint.setText(first + ", " + second);
    }

    public void LOAD(View view){
        sPref = getPreferences(MODE_PRIVATE);
        String firstSaved = sPref.getString("LIT", "");
        String secondSaved = sPref.getString("LON", "");
        startPoint.setText(firstSaved + ", " + secondSaved);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            Toast.makeText(getApplicationContext(), "Location changed", Toast.LENGTH_SHORT).show();

            if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)){
                LIT = String.format("%1$.4f", location.getLatitude());
                LON = String.format("%1$.4f", location.getLongitude());
                currentLocation.setText(LIT + ", " + LON);

                if (sPref != null){

                    sPref = getPreferences(MODE_PRIVATE);
                    String firstSaved = sPref.getString("LIT", "");
                    String secondSaved = sPref.getString("LON", "");
                    startPoint.setText(firstSaved + ", " + secondSaved);
                    Location locationA = new Location("point A");
                    Location locationB = new Location("point B");

                    if (sharedPreferences == null){
                        locationA.setLatitude(Double.parseDouble(sPref.getString("LIT", "")));
                        locationA.setLongitude(Double.parseDouble(sPref.getString("LON", "")));

                        locationB.setLatitude(Double.parseDouble(LIT));
                        locationB.setLongitude(Double.parseDouble(LON));
                    } else if (sharedPreferences != null){
                        locationA.setLatitude(Double.parseDouble(sharedPreferences.getString("PreviousLIT", "")));
                        locationA.setLongitude(Double.parseDouble(sharedPreferences.getString("PreviousLON", "")));

                        locationB.setLatitude(Double.parseDouble(LIT));
                        locationB.setLongitude(Double.parseDouble(LON));
                    }
                    float distance = locationA.distanceTo(locationB);
                    sPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sPref.edit();
                    editor.putFloat("PreviousDistance", distance);
                    float currentDistance = distance + sPref.getFloat("PreviousDistance", 0);
                    if (currentDistance >= 1000){
                        currentDistance /= 1000;
                        distanceTV.setText(String.valueOf(String.format("%1$.2f", currentDistance)) + " km");
                    } else{
                        distanceTV.setText(String.valueOf(String.format("%1$.1f", currentDistance)) + " m");
                    }
                } else if (sPref == null){
                    distanceTV.setText("0.0 m");
                }

                sharedPreferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor previousEditor = sharedPreferences.edit();
                previousEditor.putString("PreviousLIT", LIT);
                previousEditor.putString("PreviousLON", LON);
                previousEditor.commit();

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Please, turn on location", Toast.LENGTH_LONG).show();
        }

        @Override

        public void onProviderEnabled(String provider) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
            }
        }
    };
}
