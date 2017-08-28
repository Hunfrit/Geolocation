package com.hunfrit.geolocation.Service;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.hunfrit.geolocation.View.MainView;

/**
 * Created by Artem Shapovalov on 28.08.2017.
 */

public class Service implements LocationListener {

    private MainView view;

    public Service(MainView view){
        this.view = view;
    }


    @Override
    public void onLocationChanged(Location location) {
        view.serviceOnLocationChanged(location, true);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        view.serviceOnProviderDisabled(true);
    }

    @Override
    public void onProviderEnabled(String s) {
        view.serviceOnProviderEnabled(true);
    }

    @Override
    public void onProviderDisabled(String s) {
        view.serviceOnStatusChanged(true);
    }
}
