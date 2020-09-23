package com.ewheelers.ewheelers.Activities;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.NewGPSTracker;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Button updateBtn;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;
    String latitudeIs,longitudeIs;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        configureCameraIdle();
        latitudeIs = getIntent().getStringExtra("latit");
        longitudeIs = getIntent().getStringExtra("longi");
        //Toast.makeText(this, "lat: "+latitudeIs+" lngi: "+longitudeIs, Toast.LENGTH_SHORT).show();
        updateBtn = findViewById(R.id.update_btn);
        textView = findViewById(R.id.address_fetch);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),eStoreSettings.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                /*lat.setText(String.valueOf(latLng.latitude));
                lang.setText(String.valueOf(latLng.longitude));*/
                //Toast.makeText(MapsActivity.this, "latitude: "+latLng.latitude+", longitude: "+latLng.longitude, Toast.LENGTH_SHORT).show();
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        String zip = addressList.get(0).getPostalCode();
                        SessionPreference.clearString(MapsActivity.this,SessionPreference.shopaddress);
                        SessionPreference.saveString(MapsActivity.this,SessionPreference.shopaddress,locality);
                        SessionPreference.clearString(MapsActivity.this,SessionPreference.zipcode);
                        SessionPreference.saveString(MapsActivity.this,SessionPreference.zipcode,zip);
                        SessionPreference.clearString(MapsActivity.this,SessionPreference.latitude);
                        SessionPreference.saveString(MapsActivity.this,SessionPreference.latitude, String.valueOf(latLng.latitude));
                        SessionPreference.clearString(MapsActivity.this,SessionPreference.logitude);
                        SessionPreference.saveString(MapsActivity.this,SessionPreference.logitude, String.valueOf(latLng.longitude));
                        textView.setText(locality+" - "+zip);
                        //Toast.makeText(MapsActivity.this, locality, Toast.LENGTH_SHORT).show();


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
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
        if(!latitudeIs.isEmpty()&&!longitudeIs.isEmpty()) {
            LatLng sydney = new LatLng(Double.parseDouble(latitudeIs), Double.parseDouble(longitudeIs));
            //mMap.addMarker(new MarkerOptions().position(sydney).title(locality)).showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15));
        }
    }

}
