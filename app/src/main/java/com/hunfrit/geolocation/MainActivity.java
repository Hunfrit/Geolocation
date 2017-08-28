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

    private SharedPreferences mSPref, mSharedPreferences;
    private TextView mStartPoint, mCurrentLocation, mDistanceTV;
    private String LIT, LON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mStartPoint = (TextView) findViewById(R.id.startPoint);
        mCurrentLocation = (TextView) findViewById(R.id.currentLocation);
        mDistanceTV = (TextView) findViewById(R.id.distance);

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10, locationListener);

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

    public void startTracking(View view) {
        clearSP();
        String first = LIT;
        String second = LON;
        mSPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = mSPref.edit();
        ed.putString("LIT", first);
        ed.putString("LON", second);
        ed.commit();
        mStartPoint.setText(first + ", " + second);
    }

    public void LOAD(View view) {
        mSPref = getPreferences(MODE_PRIVATE);
        String firstSaved = mSPref.getString("LIT", "");
        String secondSaved = mSPref.getString("LON", "");
        mStartPoint.setText(firstSaved + ", " + secondSaved);
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            Toast.makeText(getApplicationContext(), "Location changed", Toast.LENGTH_SHORT).show();

            if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
                LIT = String.format("%1$.4f", location.getLatitude());
                LON = String.format("%1$.4f", location.getLongitude());
                mCurrentLocation.setText(LIT + ", " + LON);

                if (mSPref != null) {
                    mSPref = getPreferences(MODE_PRIVATE);
                    String firstSaved = mSPref.getString("LIT", "");
                    String secondSaved = mSPref.getString("LON", "");
                    mStartPoint.setText(firstSaved + ", " + secondSaved);

                    float distance = setDistance();
                    float currentDistance = 0;
                    if (mSharedPreferences == null) {
                        currentDistance = distance;
                    } else if (mSharedPreferences != null) {
                        currentDistance = distance + mSPref.getFloat("PreviousDistance", 0);
                    }
                    mSPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = mSPref.edit();
                    editor.putFloat("PreviousDistance", currentDistance);
                    editor.commit();

                    if (currentDistance >= 1000) {
                        currentDistance /= 1000;
                        mDistanceTV.setText(String.valueOf(String.format("%1$.2f", currentDistance)) + " km");
                    } else {
                        mDistanceTV.setText(String.valueOf(String.format("%1$.1f", currentDistance)) + " m");
                    }
                } else if (mSPref == null) {
                    mDistanceTV.setText("0.0 m");
                }
                setPreviousCoordination();
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

    private float setDistance (){
        Location locationA = new Location("point A");
        Location locationB = new Location("point B");

        if (mSharedPreferences == null) {
            locationA.setLatitude(Double.parseDouble(mSPref.getString("LIT", "")));
            locationA.setLongitude(Double.parseDouble(mSPref.getString("LON", "")));

            locationB.setLatitude(Double.parseDouble(LIT));
            locationB.setLongitude(Double.parseDouble(LON));
        } else if (mSharedPreferences != null) {
            locationA.setLatitude(Double.parseDouble(mSharedPreferences.getString("PreviousLIT", "")));
            locationA.setLongitude(Double.parseDouble(mSharedPreferences.getString("PreviousLON", "")));

            locationB.setLatitude(Double.parseDouble(LIT));
            locationB.setLongitude(Double.parseDouble(LON));
        }
        float distance = locationA.distanceTo(locationB);
        return distance;
    }

    private void setPreviousCoordination (){
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor previousEditor = mSharedPreferences.edit();
        previousEditor.putString("PreviousLIT", LIT);
        previousEditor.putString("PreviousLON", LON);
        previousEditor.commit();
    }

    private void clearSP(){
        mSPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = mSPref.edit();
        editor.remove("PreviousDistance");
        editor.commit();
    }
}
