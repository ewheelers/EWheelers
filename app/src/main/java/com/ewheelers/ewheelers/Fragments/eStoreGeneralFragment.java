package com.ewheelers.ewheelers.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.Activities.Home;
import com.ewheelers.ewheelers.Activities.MapsActivity;
import com.ewheelers.ewheelers.ActivityModels.Stateslist;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.Network.APIClient;
import com.ewheelers.ewheelers.Network.Model.Response.UpdateShopResponse;
import com.ewheelers.ewheelers.Network.Model.ShopPostData;
import com.ewheelers.ewheelers.Network.RestAPI;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.NewGPSTracker;
import com.ewheelers.ewheelers.Utils.SessionPreference;
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

import retrofit2.Call;
import retrofit2.Callback;

public class eStoreGeneralFragment extends Fragment implements AdapterView.OnItemSelectedListener, TextWatcher {
    EditText identifier, shopuri, shopaddress, postalcode, mobileno, free_shippingon, maxradious, maxRent, latitude, longitude;
    Spinner country_list, state_list, city_list, display_state;
    ArrayAdapter<Stateslist> contryAdapter;
    ArrayAdapter<Stateslist> stateAdapter;
    ArrayAdapter<Stateslist> cityAdapter;

    Button save_changes;
    ArrayList<Stateslist> countrieslist = new ArrayList<>();
    ArrayList<Stateslist> statelist = new ArrayList<>();
    ArrayList<Stateslist> citieslist = new ArrayList<>();
    String tokenValue, countryString, contryid, stateString, stateid, cityString, cityid;
    RelativeLayout mainlayout;
    private ProgressDialog progressDialog;
    String[] status_list = {"On", "Off"};
    String sdisp, shopid;
    NewGPSTracker newgps;
    private Context mContext;
    private double latitud;
    private double longitud;
    Button buttonMap;
    String lat, longi, shopaddre, zipcode, u_latitude, u_longitude;
    Switch edit_mode;
    String str_identifier, str_shopuri, str_mobileno, str_freeship, str_maxrad, str_maxrent;
    CheckBox doorTodoorService;
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
        mContext = getActivity();
        tokenValue = new SessionPreference().getStrings(getActivity(), SessionPreference.tokenvalue);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("General SetUp ....");
        progressDialog.setCancelable(false);
        edit_mode = v.findViewById(R.id.editMode);
        identifier = v.findViewById(R.id.identifier);
        buttonMap = v.findViewById(R.id.goMap);
        identifier.addTextChangedListener(this);
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
        country_list.setOnItemSelectedListener(this);
        doorTodoorService = v.findViewById(R.id.doorTodoorService);
        //state_list.setOnItemSelectedListener(this);
        country_list.setEnabled(false);
        state_list.setEnabled(false);
        city_list.setEnabled(false);
        display_state.setEnabled(false);
        edit_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    identifier.setEnabled(true);
                    identifier.setTextColor(getResources().getColor(R.color.colorBlack));
                    shopuri.setEnabled(true);
                    shopuri.setTextColor(getResources().getColor(R.color.colorBlack));
                    shopaddress.setEnabled(true);
                    shopaddress.setTextColor(getResources().getColor(R.color.colorBlack));
                    postalcode.setEnabled(true);
                    postalcode.setTextColor(getResources().getColor(R.color.colorBlack));
                    mobileno.setEnabled(true);
                    mobileno.setTextColor(getResources().getColor(R.color.colorBlack));
                    free_shippingon.setEnabled(true);
                    free_shippingon.setTextColor(getResources().getColor(R.color.colorBlack));
                    maxradious.setEnabled(true);
                    maxradious.setTextColor(getResources().getColor(R.color.colorBlack));
                    maxRent.setEnabled(true);
                    maxRent.setTextColor(getResources().getColor(R.color.colorBlack));
                    latitude.setEnabled(true);
                    latitude.setTextColor(getResources().getColor(R.color.colorBlack));
                    longitude.setEnabled(true);
                    longitude.setTextColor(getResources().getColor(R.color.colorBlack));
                    country_list.setEnabled(true);
                    state_list.setEnabled(true);
                    city_list.setEnabled(true);
                    display_state.setEnabled(true);
                } else {
                    identifier.setEnabled(false);
                    identifier.setTextColor(getResources().getColor(R.color.colorNavy));
                    shopuri.setEnabled(false);
                    shopuri.setTextColor(getResources().getColor(R.color.colorNavy));
                    shopaddress.setEnabled(false);
                    shopaddress.setTextColor(getResources().getColor(R.color.colorNavy));
                    postalcode.setEnabled(false);
                    postalcode.setTextColor(getResources().getColor(R.color.colorNavy));
                    mobileno.setEnabled(false);
                    mobileno.setTextColor(getResources().getColor(R.color.colorNavy));
                    free_shippingon.setEnabled(false);
                    free_shippingon.setTextColor(getResources().getColor(R.color.colorNavy));
                    maxradious.setEnabled(false);
                    maxradious.setTextColor(getResources().getColor(R.color.colorNavy));
                    maxRent.setEnabled(false);
                    maxRent.setTextColor(getResources().getColor(R.color.colorNavy));
                    latitude.setEnabled(false);
                    latitude.setTextColor(getResources().getColor(R.color.colorNavy));
                    longitude.setEnabled(false);
                    longitude.setTextColor(getResources().getColor(R.color.colorNavy));
                    country_list.setEnabled(false);
                    state_list.setEnabled(false);
                    city_list.setEnabled(false);
                    display_state.setEnabled(false);
                }
            }
        });
        str_identifier = new SessionPreference().getStrings(getActivity(), SessionPreference.identifier);
        str_shopuri = new SessionPreference().getStrings(getActivity(), SessionPreference.seourl);
        str_mobileno = new SessionPreference().getStrings(getActivity(), SessionPreference.phone);
        str_freeship = new SessionPreference().getStrings(getActivity(), SessionPreference.freeship);
        str_maxrad = new SessionPreference().getStrings(getActivity(), SessionPreference.maxsell);
        str_maxrent = new SessionPreference().getStrings(getActivity(), SessionPreference.maxrent);

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
        //getCurrentLocation();
        shopaddre = new SessionPreference().getStrings(getActivity(), SessionPreference.shopaddress);
        zipcode = new SessionPreference().getStrings(getActivity(), SessionPreference.zipcode);
        u_latitude = new SessionPreference().getStrings(getActivity(), SessionPreference.latitude);
        u_longitude = new SessionPreference().getStrings(getActivity(), SessionPreference.logitude);
        if (zipcode != null && shopaddre != null) {
            postalcode.setText(zipcode);
            shopaddress.setText(shopaddre);
        }
        if (u_latitude != null && u_longitude != null) {
            latitude.setText(u_latitude);
            longitude.setText(u_longitude);
        }
        getCountries();
        //getProfile();
        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsActivity.class);
                /*String str_identifier = identifier.getText().toString();
                String str_shopuri = shopuri.getText().toString();
                String str_mobileno = mobileno.getText().toString();
                String str_freeship = free_shippingon.getText().toString();
                String str_maxrad = maxradious.getText().toString();
                String str_maxrent = maxRent.getText().toString();*/
                SessionPreference.saveString(getActivity(), SessionPreference.identifier, identifier.getText().toString());
                SessionPreference.saveString(getActivity(), SessionPreference.seourl, shopuri.getText().toString());
                SessionPreference.saveString(getActivity(), SessionPreference.phone, mobileno.getText().toString());
                SessionPreference.saveString(getActivity(), SessionPreference.freeship, free_shippingon.getText().toString());
                SessionPreference.saveString(getActivity(), SessionPreference.maxsell, maxradious.getText().toString());
                SessionPreference.saveString(getActivity(), SessionPreference.maxrent, maxRent.getText().toString());
                i.putExtra("latit", latitude.getText().toString());
                i.putExtra("longi", longitude.getText().toString());
                startActivity(i);
                getActivity().finish();
            }
        });

        return v;
    }

    private void getCurrentLocation() {
        //mMap = googleMap;
        newgps = new NewGPSTracker(mContext, getActivity());
        if (newgps.canGetLocation()) {
            latitud = newgps.getLatitude();
            longitud = newgps.getLongitude();
            latitude.setText(String.valueOf(latitud));
            longitude.setText(String.valueOf(longitud));
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                List<Address> addressList = geocoder.getFromLocation(latitud, longitud, 1);
                if (addressList != null && addressList.size() > 0) {
                    String locality = addressList.get(0).getAddressLine(0);
                    String country = addressList.get(0).getCountryName();
                    String zip = addressList.get(0).getPostalCode();
                    shopaddress.setText(locality);
                    postalcode.setText(zip);
                    //Toast.makeText(MapsActivity.this, locality, Toast.LENGTH_SHORT).show();


                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getCountries() {
        //progressDialog.show();
        String url_link = API.countries;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        //String cartItemsCount = jsonObjectData.getString("cartItemsCount");
                        JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("countries");
                        for (int i = 0; i < jsonArrayCountries.length(); i++) {
                            JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                            String countryid = jsonObjectcountry.getString("id");
                            String countryname = jsonObjectcountry.getString("name");
                            Stateslist stateslist = new Stateslist(countryid, countryname);
                            //countrieslist.add(countryid + " - " + countryname);
                            countrieslist.add(stateslist);
                        }
                        if (countrieslist.size() != 0) {
                            contryAdapter = new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countrieslist);
                            country_list.setAdapter(contryAdapter);
                        }
                    } else {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Stateslist stateslist = (Stateslist) country_list.getSelectedItem();
        countryString = stateslist.getStatename();
        //countryString = country_list.getSelectedItem().toString();
        //getStatesNames(splitString(countryString));
        contryid = stateslist.getStateid();
        //getStatesNames(contryid);
        getProfile(contryid);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getProfile(final String contryid) {
        progressDialog.show();
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.getshopform;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("response..",response);
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObj = jsonObject1.getJSONObject("shopDetails");
                        if (jsonObj.length() == 0) {
                            progressDialog.dismiss();
                            //Toast.makeText(getActivity(), "ShopId not generated !", Toast.LENGTH_SHORT).show();
                            getStatesNames(contryid, "", "");
                            if (str_identifier != null) {
                                identifier.setText(str_identifier);
                            }if (str_shopuri != null) {
                                shopuri.setText(str_shopuri);
                            }if (str_maxrad != null) {
                                maxradious.setText(str_maxrad);
                            }if (str_maxrent != null) {
                                maxRent.setText(str_maxrent);
                            }if (str_mobileno != null) {
                                mobileno.setText(str_mobileno);
                            }if (str_freeship != null) {
                                free_shippingon.setText(str_freeship);
                            }
                        } else {
                            shopid = jsonObj.getString("shop_id");
                            SessionPreference.clearString(Objects.requireNonNull(getActivity()), SessionPreference.shopid);
                            SessionPreference.saveString(getActivity(), SessionPreference.shopid, shopid);
                            String shop_user_id = jsonObj.getString("shop_user_id");
                            String identy = jsonObj.getString("shop_identifier");
                            if (str_identifier != null) {
                                identifier.setText(str_identifier);
                            } else {
                                identifier.setText(identy);
                            }
                            String pincode = jsonObj.getString("shop_postalcode");
                            if (zipcode != null) {
                                postalcode.setText(zipcode);
                            } else {
                                postalcode.setText(pincode);
                            }
                            String uri_is = jsonObj.getString("urlrewrite_custom");
                            if (str_shopuri != null) {
                                shopuri.setText(str_shopuri);
                            } else {
                                shopuri.setText(uri_is);
                            }
                            String autoComplete = jsonObj.getString("shop_auto_complete");
                            if (shopaddre != null) {
                                shopaddress.setText(shopaddre);
                            } else {
                                shopaddress.setText(autoComplete);
                            }
                            lat = jsonObj.getString("shop_latitude");
                            //SessionPreference.clearString(getActivity(),SessionPreference.latitude);
                            if (u_latitude != null) {
                                latitude.setText(u_latitude);
                            } else {
                                latitude.setText(lat);
                            }
                            longi = jsonObj.getString("shop_longitude");
                            //SessionPreference.clearString(getActivity(),SessionPreference.logitude);
                            if (u_longitude != null) {
                                longitude.setText(u_longitude);
                            } else {
                                longitude.setText(longi);
                            }
                            String maxsell = jsonObj.getString("shop_max_sell_radius");
                            if (str_maxrad != null) {
                                maxradious.setText(str_maxrad);
                            } else {
                                maxradious.setText(maxsell);
                            }
                            String maxrent = jsonObj.getString("shop_max_rent_radius");
                            if (str_maxrent != null) {
                                maxRent.setText(str_maxrent);
                            } else {
                                maxRent.setText(maxrent);
                            }
                            String phone = jsonObj.getString("shop_phone");
                            if (str_mobileno != null) {
                                mobileno.setText(str_mobileno);
                            } else {
                                mobileno.setText(phone);
                            }
                            String stat = jsonObj.getString("shop_supplier_display_status");
                            display_state.setSelection(Integer.parseInt(stat));
                            String freeship = jsonObj.getString("shop_free_ship_upto");
                            if (str_freeship != null) {
                                free_shippingon.setText(str_freeship);
                            } else {
                                free_shippingon.setText(freeship);
                            }
                            String contrid = jsonObj.getString("shop_country_id");
                            String statid = jsonObj.getString("shop_state_id");
                            String cityid = jsonObj.getString("shop_city_id");
                            if(jsonObj.getInt("shop_isDoorToDoor") == 0){
                                doorTodoorService.setChecked(false);
                            }else{
                                doorTodoorService.setChecked(true);
                            }

                            getStatesNames(contrid, statid, cityid);
                            progressDialog.dismiss();
                        }
                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(mainlayout, "get Profile : " + msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    private void getStatesNames(final String splitString, final String state_id, final String city_id) {
        //progressDialog.show();
        statelist.clear();
        String url_link = API.states + splitString;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
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
                            Stateslist stateslistAre = new Stateslist(stateid, state_name);
                            //statelist.add(stateid + " - " + state_name);
                            statelist.add(stateslistAre);
                        }
                        if (statelist != null) {
                            stateAdapter = new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, statelist);
                            state_list.setAdapter(stateAdapter);
                            if (!state_id.isEmpty()) {
                                for (int j = 0; j < statelist.size(); j++) {
                                    if (statelist.get(j).getStateid().equals(state_id)) {
                                        state_list.setSelection(j);
                                    }
                                }
                            }
                        }
                        state_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Stateslist state = (Stateslist) state_list.getSelectedItem();
                                stateString = state.getStatename();
                                stateid = state.getStateid();
                                getCityNames(splitString, stateid, city_id);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        //progressDialog.dismiss();
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
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

    private void getCityNames(String countryid, String stateid, final String city_id) {
        citieslist.clear();
        String url_link = API.cities + countryid + "/" + stateid;
        //Log.i("cityurl:", url_link);
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
                    //String cartItemsCount = jsonObjectData.getString("cartItemsCount");
                    if (status.equals("1")) {
                        //progressDialog.dismiss();
                        JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("cities");
                        if (jsonArrayCountries.length() != 0) {
                            for (int i = 0; i < jsonArrayCountries.length(); i++) {
                                JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                                String cityid = jsonObjectcountry.getString("id");
                                String cityname = jsonObjectcountry.getString("name");
                                Stateslist stateslist = new Stateslist(cityid, cityname);
                                //citieslist.add(cityid + " - " + cityname);
                                citieslist.add(stateslist);
                            }
                            if (citieslist != null) {
                                cityAdapter = new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, citieslist);
                                city_list.setAdapter(cityAdapter);
                                if (!city_id.isEmpty()) {
                                    for (int j = 0; j < citieslist.size(); j++) {
                                        if (citieslist.get(j).getStateid().equals(city_id))
                                            city_list.setSelection(j);
                                    }
                                }

                            }
                            city_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Stateslist stateslist = (Stateslist) city_list.getSelectedItem();
                                    //cityString = city_list.getSelectedItem().toString();
                                    cityid = stateslist.getStateid();
                                    cityString = stateslist.getStatename();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            //progressDialog.dismiss();
                            Toast.makeText(getActivity(), "No cities are added to show", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String textchaned = s.toString();
        if (textchaned.equals(identifier.getEditableText().toString())) {
            shopuri.setText(textchaned);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void saveChanges() {
        progressDialog.show();
        RestAPI.SetupShop service = APIClient.getClient().create(RestAPI.SetupShop.class);
        Call<UpdateShopResponse> call = service.call(getParams(),tokenValue);
        call.enqueue(new Callback<UpdateShopResponse>() {
            @Override
            public void onResponse(Call<UpdateShopResponse> call, retrofit2.Response<UpdateShopResponse> response) {
                try {
                    if (response.body().getStatus().equals("1")) {
                        progressDialog.dismiss();
                        String shopidIS = response.body().getData().getShopId();
                        String langidIs = response.body().getData().getLangId();
                        Snackbar.make(mainlayout, response.body().getMsg(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        progressDialog.dismiss();
                        Snackbar.make(mainlayout, response.body().getMsg(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<UpdateShopResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getLocalizedMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private Map<String, String> getParams() {
        String shop_identifier = identifier.getText().toString();
        String shopUri_ = shopuri.getText().toString();
        String  shop_adress = shopaddress.getText().toString();
        String postalCode = postalcode.getText().toString();
        String phoneNumber = mobileno.getText().toString();
        String freeShippintone = free_shippingon.getText().toString();
        String maxTraRadius  = maxradious.getText().toString();
        String maxRent_ = maxRent.getText().toString();
        String latString = latitude.getText().toString();
        String longString = longitude.getText().toString();
        Map<String, String> data3 = new HashMap<String, String>();
        data3.put("shop_identifier", shop_identifier);
        data3.put("urlrewrite_custom", shopUri_);
        data3.put("shop_auto_complete", shop_adress);
        data3.put("shop_postalcode", postalCode);
        data3.put("shop_phone", phoneNumber);
        data3.put("shop_country_id", contryid);
        data3.put("shop_state", stateid);
        data3.put("shop_city_id", cityid);
        if (sdisp.equals("On")) {
            data3.put("shop_supplier_display_status", "0");
        } else {
            data3.put("shop_supplier_display_status", "1");
        }
        data3.put("shop_free_ship_upto", freeShippintone);
        data3.put("shop_max_sell_radius", maxTraRadius);
        data3.put("shop_max_rent_radius", maxRent_);
        data3.put("shop_latitude", latString);
        data3.put("shop_longitude", longString);
        if(doorTodoorService.isChecked()){
            data3.put("shop_isDoorToDoor","1");
        }else{
            data3.put("shop_isDoorToDoor","0");
        }

        if (shopid == null) {
            data3.put("shop_id", "");
        } else {
            data3.put("shop_id", shopid);
        }
        Log.d("Data...",data3.toString());
        return data3;
    };

}
