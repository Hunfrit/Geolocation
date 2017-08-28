package com.hunfrit.geolocation.View;

import android.location.Location;
import android.location.LocationListener;

/**
 * Created by Artem Shapovalov on 28.08.2017.
 */

public interface MainView {

    void serviceOnLocationChanged(Location location, boolean check);

    void serviceOnProviderDisabled(boolean check);

    void serviceOnProviderEnabled(boolean check);

    void serviceOnStatusChanged(boolean check);


}
