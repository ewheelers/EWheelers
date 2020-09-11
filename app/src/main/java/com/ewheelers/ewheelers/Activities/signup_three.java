package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class signup_three extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;
    EditText city,state,pincode;
    Button regapproval;
    String user_id,b_name,p_name,p_contact,b_address1,b_address2,b_state,b_city,b_pincode;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_three);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        regapproval = findViewById(R.id.next_form);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        configureCameraIdle();
        progressDialog = new ProgressDialog(signup_three.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Business SetUP...");
        progressDialog.setCancelable(false);
        regapproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = new SessionPreference().getStrings(signup_three.this,SessionPreference.userid);
                b_name = new SessionPreference().getStrings(signup_three.this,SessionPreference.businessname);
                p_name = new SessionPreference().getStrings(signup_three.this,SessionPreference.personname);
                p_contact = new SessionPreference().getStrings(signup_three.this,SessionPreference.mobileno);
                b_address1 = new SessionPreference().getStrings(signup_three.this,SessionPreference.addressone);
                b_address2 = new SessionPreference().getStrings(signup_three.this,SessionPreference.addresstwo);
                b_state = state.getText().toString();
                b_city = city.getText().toString();
                b_pincode = pincode.getText().toString();
                if(b_city.isEmpty()||b_state.isEmpty()||b_pincode.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(v, "Drag the map to set fields or enter manually (if map not works due to network issue)", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    registerApproval(v,user_id,b_name,p_name,p_contact,b_address1,b_address2,b_state,b_city,b_pincode);
                }

            }
        });
    }

    private void registerApproval(final View v,final String user_id, final String b_name, final String p_name, final String p_contact, final String b_address1, final String b_address2, final String b_state, final String b_city, final String b_pincode) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = API.businesssetup;
        progressDialog.show();
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int getStatus = Integer.parseInt(jsonObject.getString("status"));
                            String smsg = jsonObject.getString("msg");
                            if (getStatus!=0) {
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                Intent i = new Intent(signup_three.this,LoginScreenActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

          /*  @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-USER-TYPE", reg_user_name.getText().toString());
                return params;
            }*/

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("sformfield_1", b_name);
                data3.put("sformfield_2", p_name);
                data3.put("sformfield_3", p_contact);
                data3.put("sformfield_11", b_address1);
                data3.put("sformfield_12", b_address2);
                data3.put("sformfield_13", b_state);
                data3.put("sformfield_14", b_city);
                data3.put("sformfield_15", b_pincode);
                data3.put("id", user_id);

                return data3;

            }
        };

        queue.add(strRequest);

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
