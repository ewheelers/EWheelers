package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.EditText;

import com.ewheelers.ewheelers.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class signup_three extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;
    EditText city,state,pincode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_three);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        configureCameraIdle();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
       /* mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);*/
        mMap.setMyLocationEnabled(true);
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(signup_three.this);
                /*lat.setText(String.valueOf(latLng.latitude));
                lang.setText(String.valueOf(latLng.longitude));*/
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        String zip = addressList.get(0).getPostalCode();
                        String stat = addressList.get(0).getAdminArea();
                        String citi = addressList.get(0).getLocality();
                        city.setText(citi);
                        state.setText(stat);
                        pincode.setText(zip);
                        /*if (!locality.isEmpty() && !country.isEmpty())
                            city.setText(citi);
                        postalcode.setText(zip);*/
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

}
