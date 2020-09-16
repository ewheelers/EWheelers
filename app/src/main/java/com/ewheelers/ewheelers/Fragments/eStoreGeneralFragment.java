package com.ewheelers.ewheelers.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.Activities.BankAccountDetails;
import com.ewheelers.ewheelers.Activities.SetupAccount;
import com.ewheelers.ewheelers.Activities.UpdateAttributes;
import com.ewheelers.ewheelers.ActivityModels.Stateslist;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.view.View.GONE;
import static com.ewheelers.ewheelers.Utils.SessionPreference.cityname;

public class eStoreGeneralFragment extends Fragment implements OnMapReadyCallback , AdapterView.OnItemSelectedListener {
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GoogleMap.OnCameraIdleListener onCameraIdleListener;
    EditText identifier,shopuri,shopaddress,postalcode,mobileno,free_shippingon,maxradious,maxRent,latitude,longitude;
    Spinner country_list,state_list,city_list, display_state;
    Button save_changes;
    ArrayList<Stateslist> countrieslist = new ArrayList<>();
    ArrayList<Stateslist> statelist = new ArrayList<>();
    ArrayList<Stateslist> citieslist = new ArrayList<>();
    String tokenValue, countryString,contryid, stateString,stateid, cityString, cityid;
    RelativeLayout mainlayout;
    private ProgressDialog progressDialog;
    String[] status_list = {"On","Off"};
    String sdisp,shopid;
    RelativeLayout relativeLayout;
    public eStoreGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_e_store_general, container, false);
        tokenValue = new SessionPreference().getStrings(getActivity(),SessionPreference.tokenvalue);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("General SetUp ....");
        progressDialog.setCancelable(false);
        configureCameraIdle();
        identifier = v.findViewById(R.id.identifier);
        shopuri = v.findViewById(R.id.seourl);
        shopaddress = v.findViewById(R.id.shopaddress);
        postalcode = v.findViewById(R.id.postalcode);
        mobileno = v.findViewById(R.id.phone);
        country_list = v.findViewById(R.id.contry);
        state_list = v.findViewById(R.id.state);
        city_list = v.findViewById(R.id.city_name);
        display_state = v.findViewById(R.id.display_status);
        free_shippingon = v.findViewById(R.id.shippingon);
        maxradious = v.findViewById(R.id.maxradious);
        maxRent = v.findViewById(R.id.maxRent);
        latitude = v.findViewById(R.id.latitude);
        longitude = v.findViewById(R.id.logitude);
        save_changes = v.findViewById(R.id.next_three);
        mainlayout = v.findViewById(R.id.main_layout);
        relativeLayout = v.findViewById(R.id.relay);
        country_list.setOnItemSelectedListener(this);
        state_list.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, status_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        display_state.setAdapter(adapter);
        display_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sdisp = display_state.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getProfile();
        getCountries();
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        return v;
    }

    private void getProfile() {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.getshopform;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("shopDetails");
                        if(jsonObject3.length()==0){

                        }else {
                            shopid = jsonObject3.getString("shop_id");
                            SessionPreference.saveString(getActivity(),SessionPreference.shopid,shopid);
                            String shop_user_id = jsonObject3.getString("shop_user_id");
                            String identy = jsonObject3.getString("shop_identifier");
                            identifier.setText(identy);
                            String pincode = jsonObject3.getString("shop_postalcode");
                            postalcode.setText(pincode);
                            String uri_is = jsonObject3.getString("urlrewrite_custom");
                            shopuri.setText(uri_is);
                            String autoComplete = jsonObject3.getString("shop_auto_complete");
                            shopaddress.setText(autoComplete);
                            String latitud = jsonObject3.getString("shop_latitude");
                            latitude.setText(latitud);
                            String longitud = jsonObject3.getString("shop_longitude");
                            longitude.setText(longitud);
                            String maxsell = jsonObject3.getString("shop_max_sell_radius");
                            maxradious.setText(maxsell);
                            String maxrent = jsonObject3.getString("shop_max_rent_radius");
                            maxRent.setText(maxrent);
                            String phone = jsonObject3.getString("shop_phone");
                            mobileno.setText(phone);
                            String stat = jsonObject3.getString("shop_supplier_display_status");
                            display_state.setSelection(Integer.parseInt(stat));
                            String freeship = jsonObject3.getString("shop_free_ship_upto");
                            free_shippingon.setText(freeship);
                            String countryd = jsonObject3.getString("shop_country_id");
                            String stated = jsonObject3.getString("shop_state_id");
                            String cityd = jsonObject3.getString("shop_city_id");
                            contryid = countryd;
                            stateid = stated;
                            cityid = cityd;
                        }
                    }

                    else {
                        Snackbar.make(mainlayout, msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }


            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);

    }

    private void saveChanges() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = API.setupShop;
        progressDialog.show();
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String smsg = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                progressDialog.dismiss();
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                String shopidIS = jsonObjectData.getString("shopId");
                                String langidIs = jsonObjectData.getString("langId");
                                Snackbar.make(mainlayout, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(mainlayout, smsg, Snackbar.LENGTH_LONG)
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("shop_identifier", identifier.getText().toString());
                data3.put("urlrewrite_custom", shopuri.getText().toString());
                data3.put("shop_auto_complete", shopaddress.getText().toString());
                data3.put("shop_postalcode", postalcode.getText().toString());
                data3.put("shop_phone", mobileno.getText().toString());
                data3.put("shop_country_id", countryString);
                data3.put("shop_state",  stateString);
                data3.put("shop_city_id", cityString);
                if(sdisp.equals("On")){
                    data3.put("shop_supplier_display_status", "0");
                }else {
                    data3.put("shop_supplier_display_status", "1");
                }
                data3.put("shop_free_ship_upto", free_shippingon.getText().toString());
                data3.put("shop_max_sell_radius", maxradious.getText().toString());
                data3.put("shop_max_rent_radius", maxRent.getText().toString());
                data3.put("shop_latitude", latitude.getText().toString());
                data3.put("shop_longitude", longitude.getText().toString());
                if(shopid==null){
                    data3.put("shop_id", "");
                }else {
                    data3.put("shop_id", shopid);
                }
                return data3;

            }
        };

        queue.add(strRequest);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(onCameraIdleListener);
        /*mMap.setMinZoomPreference(6.0f);
        mMap.setMaxZoomPreference(14.0f);*/
        mMap.setMyLocationEnabled(true);
    }

    private void configureCameraIdle() {
        onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(getActivity());
                latitude.setText(String.valueOf(latLng.latitude));
                longitude.setText(String.valueOf(latLng.longitude));
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        String locality = addressList.get(0).getAddressLine(0);
                        String country = addressList.get(0).getCountryName();
                        String zip = addressList.get(0).getPostalCode();
                        String stat = addressList.get(0).getAdminArea();
                        String citi = addressList.get(0).getLocality();
                        shopaddress.setText(locality);
                        postalcode.setText(zip);
                        /*city.setText(citi);
                        state.setText(stat);
                        pincode.setText(zip);*/

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String splitString(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
        return String.valueOf(num);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String splitStringAlpha(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
        return String.valueOf(alpha);
    }

    private void getCountries() {
        String url_link = API.countries;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("countries");
                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        String countryname = jsonObjectcountry.getString("name");
                        Stateslist stateslist = new Stateslist(countryid,countryname);
                        //countrieslist.add(countryid + " - " + countryname);
                        countrieslist.add(stateslist);
                    }

                    country_list.setAdapter(new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countrieslist));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Stateslist stateslist = (Stateslist) country_list.getSelectedItem();
        countryString = stateslist.getStatename();
        //countryString = country_list.getSelectedItem().toString();
        //getStatesNames(splitString(countryString));
        contryid = stateslist.getStateid();
        getStatesNames(contryid);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void getStatesNames(final String splitString) {
        statelist.clear();
        String url_link = API.states + splitString;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    final String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("states");

                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        JSONObject countryname = jsonObjectcountry.getJSONObject("name");
                        String stateid = countryname.getString("state_id");
                        String state_code = countryname.getString("state_code");
                        String state_name = countryname.getString("state_name");
                        Stateslist stateslistAre = new Stateslist(stateid,state_name);
                        //statelist.add(stateid + " - " + state_name);
                        statelist.add(stateslistAre);
                    }

                    state_list.setAdapter(new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, statelist));
                    state_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Stateslist state = (Stateslist) state_list.getSelectedItem();
                            stateString = state.getStatename();
                            //stateString = state_list.getSelectedItem().toString();
                            //Toast.makeText(SetupBillingAddressActivity.this, splitString(stateString), Toast.LENGTH_SHORT).show();
                            //getCityNames(splitString(countryString), splitString(stateString));
                            stateid = state.getStateid();
                            getCityNames(splitString,stateid);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

    private void getCityNames(String countryid, String stateid) {
        citieslist.clear();
        String url_link = API.cities + countryid + "/" + stateid;
        Log.i("cityurl:", url_link);
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("cities");
                    if (jsonArrayCountries != null) {
                        for (int i = 0; i < jsonArrayCountries.length(); i++) {
                            JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                            String cityid = jsonObjectcountry.getString("id");
                            String cityname = jsonObjectcountry.getString("name");
                            Stateslist stateslist = new Stateslist(cityid,cityname);
                            //citieslist.add(cityid + " - " + cityname);
                            citieslist.add(stateslist);
                        }
                        city_list.setAdapter(new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, citieslist));
                        city_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Stateslist stateslist = (Stateslist) city_list.getSelectedItem();
                                //cityString = city_list.getSelectedItem().toString();
                                cityString = stateslist.getStatename();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {
                        Toast.makeText(getActivity(), "No cities are added to show", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


}
