package com.ewheelers.ewheelers.Activities;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.ewheelers.ewheelers.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;
    String latitude, longitude;
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latitude = getIntent().getStringExtra("latit");
        longitude = getIntent().getStringExtra("longi");
        configureCameraIdle();
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
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
       /* LatLng sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        mMap.addMarker(new MarkerOptions().position(sydney).title(address(latitude, longitude))).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,11));*/
    }

    private String address(String latitude, String longitude) {
        String addressIs="";
        geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addressList = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
            if (addressList != null && addressList.size() > 0) {
                addressIs = addressList.get(0).getAddressLine(0);
                String country = addressList.get(0).getCountryName();
                String zip = addressList.get(0).getPostalCode();
                String stat = addressList.get(0).getAdminArea();
                String citi = addressList.get(0).getLocality();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressIs;
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String address = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        String zip = addressList.get(0).getPostalCode();
                        String stat = addressList.get(0).getAdminArea();
                        String citi = addressList.get(0).getLocality();

                        new MarkerOptions().position(latLng).title(address);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

}
